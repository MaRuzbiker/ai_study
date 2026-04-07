<template>
  <div class="word-page">
    <!-- 顶部栏 -->
    <div class="top-bar">
      <div class="top-bar-left">
        <span class="app-title">背单词</span>
      </div>
      <div class="top-bar-right">
        <button class="icon-btn" @click="openSettings" title="设置">
          <i class="el-icon-setting"></i>
        </button>
      </div>
    </div>

    <!-- 欢迎界面 -->
    <div v-if="!showLearning" class="welcome-section">
      <div class="progress-card">
        <div class="progress-label">今日学习进度</div>
        <div class="progress-row">
          <span class="current">{{ learnedToday }}</span>
          <span class="sep">/</span>
          <span class="goal">{{ dailyGoal }}</span>
        </div>
        <div class="progress-bar-bg">
          <div class="progress-bar-fill" :style="{ width: progressPercent + '%' }"></div>
        </div>
        <div class="remaining">剩余 {{ remainingCount }} 个单词</div>
      </div>

      <button class="start-btn" @click="startLearning">
        <i class="el-icon-video-play"></i> 开始学习
      </button>

      <button class="wrong-btn" @click="$router.push('/wrong-book')">
        <i class="el-icon-document"></i> 我的错词本 ({{ wrongCount }})
      </button>
    </div>

    <!-- 学习界面 -->
    <div v-else class="learning-section">
      <!-- 进度条 -->
      <div class="learn-progress">
        <span>进度: {{ learnedToday }}/{{ dailyGoal }}</span>
        <span>剩余 {{ remainingCount }}</span>
      </div>

      <!-- 单词卡片 -->
      <div class="word-card">
        <!-- 闪卡模式：显示单词，点击后显示释义 -->
        <template v-if="currentMode === 'flashcard'">
          <div class="word-text">{{ currentWord.word }}</div>
          <div v-if="currentWord.phonetic" class="phonetic-text">{{ currentWord.phonetic }}</div>
          
          <div v-if="showMeaning" class="meaning-text">{{ currentWord.meaning }}</div>
          
          <button v-if="!showMeaning" class="reveal-btn" @click="showMeaning = true">
            显示释义
          </button>
          
          <template v-if="showMeaning">
            <div class="action-row">
              <button class="unk-btn" @click="markUnknown">不认识</button>
              <button class="knw-btn" @click="markKnown">认识</button>
            </div>
          </template>
        </template>

        <!-- 填空模式：只显示释义，隐藏单词 -->
        <template v-if="currentMode === 'fillBlank'">
          <div class="mode-hint">根据释义填写单词</div>
          <div class="meaning-prompt">{{ currentWord.meaning }}</div>
          
          <div class="hint-text">
            提示：首字母 <span class="hl">{{ currentWord.word.charAt(0).toUpperCase() }}</span>，
            共 {{ currentWord.word.length }} 个字母
          </div>
          
          <input
            v-model="fillBlankAnswer"
            class="answer-input"
            :placeholder="fillBlankChecked ? '' : '请输入单词'"
            :disabled="fillBlankChecked"
            @keyup.enter="checkFillBlank"
          />
          
          <div v-if="fillBlankChecked" class="feedback" :class="fillBlankCorrect ? 'ok' : 'err'">
            {{ fillBlankCorrect ? '正确！' : '正确答案：' + currentWord.word }}
          </div>
          
          <template v-if="fillBlankChecked">
            <div class="action-row">
              <button class="unk-btn" @click="markUnknown">不认识</button>
              <button class="knw-btn" @click="markKnown">认识</button>
            </div>
          </template>
          <template v-else>
            <div class="action-row-single">
              <button class="check-btn" @click="checkFillBlank">检查</button>
              <button class="skip-btn" @click="revealFillBlank">显示答案</button>
            </div>
          </template>
        </template>

        <!-- 拼写模式：只显示释义，隐藏单词 -->
        <template v-if="currentMode === 'spelling'">
          <div class="mode-hint">根据释义拼写单词</div>
          <div class="meaning-prompt">{{ currentWord.meaning }}</div>
          
          <input
            v-model="spellingAnswer"
            class="answer-input"
            :placeholder="spellingChecked ? '' : '请拼写单词'"
            :disabled="spellingChecked"
            @keyup.enter="checkSpelling"
          />
          
          <div v-if="spellingChecked" class="feedback" :class="spellingCorrect ? 'ok' : 'err'">
            {{ spellingCorrect ? '正确！' : '正确答案：' + currentWord.word }}
          </div>
          
          <template v-if="spellingChecked">
            <div class="action-row">
              <button class="unk-btn" @click="markUnknown">不认识</button>
              <button class="knw-btn" @click="markKnown">认识</button>
            </div>
          </template>
          <template v-else>
            <div class="action-row-single">
              <button class="check-btn" @click="checkSpelling">检查</button>
              <button class="skip-btn" @click="revealSpelling">显示答案</button>
            </div>
          </template>
        </template>
      </div>

      <!-- 模式切换 -->
      <div class="mode-bar">
        <button
          v-for="m in modes"
          :key="m.value"
          class="mode-btn"
          :class="{ active: currentMode === m.value }"
          @click="switchMode(m.value)"
        >
          {{ m.label }}
        </button>
      </div>
    </div>

    <!-- 设置弹窗 -->
    <div v-if="showSettings" class="modal-mask" @click="showSettings = false">
      <div class="modal" @click.stop>
        <div class="modal-hd">
          <span>设置</span>
          <button class="x-btn" @click="showSettings = false">×</button>
        </div>
        <div class="modal-bd">
          <div class="field">
            <label>每日学习目标（个）</label>
            <input v-model.number="settings.dailyGoal" type="number" min="1" max="200" />
          </div>
          <div class="field">
            <label>提醒时间</label>
            <input v-model="settings.reminderTime" type="time" />
          </div>
        </div>
        <div class="modal-ft">
          <button class="cancel-btn" @click="showSettings = false">取消</button>
          <button class="save-btn" @click="saveSettings">保存</button>
        </div>
      </div>
    </div>

    <!-- Toast 提示 -->
    <div v-if="toast.show" class="toast-msg">{{ toast.message }}</div>

    <!-- 庆祝动画 -->
    <div v-if="showFireworks" class="fireworks">
      <div class="congrats">🎉 恭喜达成今日目标！🎉</div>
      <button class="close-fireworks" @click="showFireworks = false">×</button>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref, computed, onMounted } from 'vue';
import { ElMessage } from 'element-plus';
import { get, post } from '@/api/index';

interface WordBook { id: number; name: string; category: string; wordCount: number; }
interface Word { id: number; word: string; phonetic: string; meaning: string; }

const modes = [
  { value: 'flashcard', label: '闪卡' },
  { value: 'fillBlank', label: '填空' },
  { value: 'spelling', label: '拼写' }
];

const books = ref<WordBook[]>([]);
const selectedBookId = ref<number>(2);
const showLearning = ref(false);
const showSettings = ref(false);
const settings = ref({ dailyGoal: 20, reminderTime: '' });

const wordList = ref<Word[]>([]);
const currentIndex = ref(0);
const currentMode = ref('flashcard');
const showMeaning = ref(false);
const fillBlankAnswer = ref('');
const fillBlankChecked = ref(false);
const fillBlankCorrect = ref(false);
const spellingAnswer = ref('');
const spellingChecked = ref(false);
const spellingCorrect = ref(false);

const learnedToday = ref(0);
const dailyGoal = ref(20);
const wrongCount = ref(0);
const toast = ref({ show: false, message: '' });
const showFireworks = ref(false);

const currentWord = computed(() => wordList.value[currentIndex.value] || { id: 0, word: '', phonetic: '', meaning: '' });
const progressPercent = computed(() => Math.min((learnedToday.value / dailyGoal.value) * 100, 100));
const remainingCount = computed(() => Math.max(dailyGoal.value - learnedToday.value, 0));

const loadBooks = async () => {
  try {
    const res = await get<WordBook[]>('/words/books');
    if (res.code === 0) books.value = res.data || [];
  } catch (e) { console.error(e); }
};

const loadProgress = () => {
  const p = localStorage.getItem('dailyProgress');
  if (p) {
    const d = JSON.parse(p);
    if (new Date(d.date).toDateString() === new Date().toDateString()) {
      learnedToday.value = d.count;
    }
  }
  const s = localStorage.getItem('vocabularySettings');
  if (s) {
    const ps = JSON.parse(s);
    dailyGoal.value = ps.dailyGoal || 20;
    settings.value = { ...settings.value, ...ps };
  }
};

const saveProgress = () => {
  localStorage.setItem('dailyProgress', JSON.stringify({ date: new Date().toISOString(), count: learnedToday.value }));
};

const openSettings = () => { showSettings.value = true; };

const saveSettings = () => {
  dailyGoal.value = settings.value.dailyGoal;
  localStorage.setItem('vocabularySettings', JSON.stringify(settings.value));
  showSettings.value = false;
  showToast('设置已保存');
};

const startLearning = async () => {
  try {
    const res = await get<Word[]>('/words/review', { bookId: selectedBookId.value, limit: dailyGoal.value });
    if (res.code === 0 && res.data && res.data.length > 0) {
      wordList.value = res.data;
      currentIndex.value = 0;
      resetState();
      showLearning.value = true;
    } else {
      ElMessage.warning('该词书暂无单词可学习');
    }
  } catch (e) { ElMessage.error('加载单词失败'); }
};

const resetState = () => {
  showMeaning.value = false;
  fillBlankAnswer.value = '';
  fillBlankChecked.value = false;
  fillBlankCorrect.value = false;
  spellingAnswer.value = '';
  spellingChecked.value = false;
  spellingCorrect.value = false;
};

const switchMode = (mode: string) => {
  currentMode.value = mode;
  resetState();
};

// 填空模式：检查答案
const checkFillBlank = () => {
  if (fillBlankChecked.value) return;
  
  const userAnswer = fillBlankAnswer.value.trim().toLowerCase();
  const correctAnswer = currentWord.value.word.toLowerCase();
  
  fillBlankChecked.value = true;
  fillBlankCorrect.value = userAnswer === correctAnswer;
  
  if (!fillBlankCorrect.value) {
    fillBlankAnswer.value = currentWord.value.word;
    showToast('正确答案: ' + currentWord.value.word);
  }
};

// 填空模式：显示答案
const revealFillBlank = () => {
  fillBlankAnswer.value = currentWord.value.word;
  fillBlankChecked.value = true;
  fillBlankCorrect.value = false;
  showToast('正确答案: ' + currentWord.value.word);
};

// 拼写模式：检查答案
const checkSpelling = () => {
  if (spellingChecked.value) return;
  
  const userAnswer = spellingAnswer.value.trim().toLowerCase();
  const correctAnswer = currentWord.value.word.toLowerCase();
  
  spellingChecked.value = true;
  spellingCorrect.value = userAnswer === correctAnswer;
  
  if (!spellingCorrect.value) {
    spellingAnswer.value = currentWord.value.word;
    showToast('正确答案: ' + currentWord.value.word);
  }
};

// 拼写模式：显示答案
const revealSpelling = () => {
  spellingAnswer.value = currentWord.value.word;
  spellingChecked.value = true;
  spellingCorrect.value = false;
  showToast('正确答案: ' + currentWord.value.word);
};

// 标记为认识
const markKnown = async () => {
  const justAchieved = learnedToday.value === dailyGoal.value - 1;
  learnedToday.value++;
  saveProgress();
  
  if (justAchieved) {
    showFireworks.value = true;
    setTimeout(() => { showFireworks.value = false; }, 5000);
  }
  
  // 记录到后端
  if (currentWord.value.id && selectedBookId.value) {
    try { 
      await post('/words/correct', null, { params: { wordId: currentWord.value.id, bookId: selectedBookId.value } }); 
    } catch (e) {}
  }
  
  nextWord();
};

// 标记为不认识 - 加入错词本
const markUnknown = async () => {
  // 记录到后端错词本
  if (currentWord.value.id && selectedBookId.value) {
    try { 
      await post('/words/wrong', null, { params: { wordId: currentWord.value.id, bookId: selectedBookId.value } }); 
      wrongCount.value++;
      showToast('已加入错词本');
    } catch (e) {
      console.error('记录错词失败:', e);
    }
  }
  
  nextWord();
};

const nextWord = () => {
  resetState();
  if (currentIndex.value < wordList.value.length - 1) {
    currentIndex.value++;
  } else {
    showLearning.value = false;
    ElMessage.success('本轮学习完成！');
  }
};

const showToast = (msg: string) => {
  toast.value = { show: true, message: msg };
  setTimeout(() => { toast.value.show = false; }, 2000);
};

onMounted(() => { loadBooks(); loadProgress(); });
</script>

<style scoped>
.word-page { min-height: 100%; padding: 0; background: transparent; }

/* 顶部栏 */
.top-bar { display: flex; align-items: center; justify-content: space-between; padding: 12px 16px; background: linear-gradient(135deg, #667eea, #764ba2); border-radius: 12px; margin-bottom: 16px; }
.app-title { font-size: 18px; font-weight: bold; color: white; }
.top-bar-right { display: flex; gap: 8px; }
.icon-btn { width: 36px; height: 36px; border-radius: 50%; background: rgba(255,255,255,0.2); border: none; color: white; font-size: 16px; cursor: pointer; display: flex; align-items: center; justify-content: center; }
.icon-btn:hover { background: rgba(255,255,255,0.35); }

/* 欢迎界面 */
.welcome-section { padding: 8px 0; }
.progress-card { background: linear-gradient(135deg, #667eea, #764ba2); border-radius: 16px; padding: 20px; color: white; margin-bottom: 16px; }
.progress-label { font-size: 13px; opacity: 0.9; margin-bottom: 6px; }
.progress-row { font-size: 32px; font-weight: bold; margin-bottom: 10px; }
.sep { opacity: 0.6; margin: 0 4px; }
.progress-bar-bg { height: 6px; background: rgba(255,255,255,0.3); border-radius: 3px; overflow: hidden; margin-bottom: 6px; }
.progress-bar-fill { height: 100%; background: white; border-radius: 3px; transition: width 0.4s; }
.remaining { font-size: 12px; opacity: 0.8; }
.start-btn { width: 100%; padding: 14px; background: linear-gradient(135deg, #10b981, #059669); color: white; border: none; border-radius: 14px; font-size: 16px; font-weight: bold; cursor: pointer; margin-bottom: 10px; display: flex; align-items: center; justify-content: center; gap: 6px; }
.wrong-btn { width: 100%; padding: 12px; background: white; color: #667eea; border: 2px solid #667eea; border-radius: 14px; font-size: 14px; font-weight: 600; cursor: pointer; display: flex; align-items: center; justify-content: center; gap: 6px; }

/* 学习界面 */
.learning-section { padding: 8px 0; }
.learn-progress { display: flex; justify-content: space-between; font-size: 13px; color: #909399; margin-bottom: 12px; }
.word-card { background: white; border-radius: 16px; padding: 28px 20px; box-shadow: 0 2px 12px rgba(0,0,0,0.08); text-align: center; margin-bottom: 12px; }

/* 闪卡模式 */
.word-text { font-size: 44px; font-weight: bold; color: #303133; margin-bottom: 8px; }
.phonetic-text { font-size: 16px; color: #909399; margin-bottom: 12px; }
.meaning-text { font-size: 20px; color: #667eea; margin-bottom: 16px; line-height: 1.5; }
.reveal-btn { padding: 10px 28px; background: linear-gradient(135deg, #667eea, #764ba2); color: white; border: none; border-radius: 8px; font-size: 14px; cursor: pointer; }

/* 填空/拼写模式 */
.mode-hint { font-size: 14px; color: #909399; margin-bottom: 10px; }
.meaning-prompt { font-size: 20px; color: #303133; margin-bottom: 16px; line-height: 1.6; font-weight: 500; }
.hint-text { font-size: 13px; color: #909399; margin-bottom: 10px; }
.hl { color: #667eea; font-weight: bold; }
.answer-input { width: 100%; padding: 12px; font-size: 18px; border: 2px solid #e4e7ed; border-radius: 10px; text-align: center; outline: none; margin-bottom: 8px; box-sizing: border-box; }
.answer-input:focus { border-color: #667eea; }
.feedback { padding: 8px; border-radius: 6px; font-size: 14px; margin-bottom: 12px; }
.feedback.ok { background: #d1fae5; color: #065f46; }
.feedback.err { background: #fee2e2; color: #991b1b; }

/* 操作按钮 */
.action-row { display: flex; gap: 12px; margin-top: 16px; }
.unk-btn, .knw-btn { flex: 1; padding: 12px; border: none; border-radius: 10px; font-size: 15px; font-weight: bold; cursor: pointer; }
.unk-btn { background: #ef4444; color: white; }
.knw-btn { background: #10b981; color: white; }

.action-row-single { display: flex; gap: 10px; margin-top: 16px; }
.check-btn { flex: 1; padding: 12px; background: linear-gradient(135deg, #667eea, #764ba2); color: white; border: none; border-radius: 10px; font-size: 15px; font-weight: bold; cursor: pointer; }
.skip-btn { padding: 12px 20px; background: #f5f5f5; color: #909399; border: none; border-radius: 10px; font-size: 14px; cursor: pointer; }

/* 模式切换 */
.mode-bar { display: flex; gap: 6px; background: white; border-radius: 10px; padding: 4px; box-shadow: 0 1px 4px rgba(0,0,0,0.06); }
.mode-btn { flex: 1; padding: 8px; border: none; background: transparent; border-radius: 7px; font-size: 13px; color: #909399; cursor: pointer; transition: all 0.2s; }
.mode-btn.active { background: #667eea; color: white; }

/* 弹窗 */
.modal-mask { position: fixed; inset: 0; background: rgba(0,0,0,0.5); display: flex; align-items: center; justify-content: center; z-index: 200; }
.modal { background: white; border-radius: 16px; width: 90%; max-width: 340px; overflow: hidden; }
.modal-hd { display: flex; justify-content: space-between; align-items: center; padding: 16px 20px; border-bottom: 1px solid #f0f0f0; font-size: 16px; font-weight: bold; color: #303133; }
.x-btn { width: 28px; height: 28px; border-radius: 50%; background: #f5f5f5; border: none; cursor: pointer; font-size: 16px; color: #909399; }
.modal-bd { padding: 16px 20px; }
.field { margin-bottom: 14px; }
.field label { display: block; font-size: 13px; color: #606266; margin-bottom: 5px; }
.field input { width: 100%; padding: 8px 10px; border: 1px solid #dcdfe6; border-radius: 6px; font-size: 14px; box-sizing: border-box; }
.modal-ft { display: flex; gap: 10px; padding: 12px 20px; border-top: 1px solid #f0f0f0; }
.cancel-btn { flex: 1; padding: 10px; background: #f5f5f5; color: #606266; border: none; border-radius: 8px; font-size: 14px; cursor: pointer; }
.save-btn { flex: 1; padding: 10px; background: linear-gradient(135deg, #667eea, #764ba2); color: white; border: none; border-radius: 8px; font-size: 14px; font-weight: bold; cursor: pointer; }

/* Toast */
.toast-msg { position: fixed; bottom: 40px; left: 50%; transform: translateX(-50%); background: rgba(0,0,0,0.75); color: white; padding: 10px 22px; border-radius: 8px; font-size: 14px; z-index: 300; }

/* 庆祝动画 */
.fireworks { position: fixed; inset: 0; background: rgba(0,0,0,0.7); display: flex; align-items: center; justify-content: center; z-index: 300; }
.congrats { font-size: 28px; color: white; font-weight: bold; text-align: center; animation: pop 1s infinite; }
@keyframes pop { 0%,100%{transform:scale(1)} 50%{transform:scale(1.05)} }
.close-fireworks { position: absolute; top: 30px; right: 30px; width: 36px; height: 36px; border-radius: 50%; background: rgba(255,255,255,0.2); border: 2px solid white; color: white; font-size: 18px; cursor: pointer; }
</style>
