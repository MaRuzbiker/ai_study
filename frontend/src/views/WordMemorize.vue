<template>
  <div class="word-page">
    <!-- Top Bar -->
    <div class="top-bar">
      <div class="top-bar-left">
        <span class="app-title">Word Master</span>
      </div>
      <div class="top-bar-right">
        <button class="icon-btn" @click="openSettings">
          <i class="el-icon-setting"></i>
        </button>
      </div>
    </div>

    <!-- Welcome View -->
    <div v-if="!showLearning" class="welcome-section">
      <div class="progress-card">
        <div class="progress-label">Today's Progress</div>
        <div class="progress-row">
          <span class="current">{{ learnedToday }}</span>
          <span class="sep">/</span>
          <span class="goal">{{ dailyGoal }}</span>
        </div>
        <div class="progress-bar-bg">
          <div class="progress-bar-fill" :style="{ width: progressPercent + '%' }"></div>
        </div>
        <div class="remaining">{{ remainingCount }} words remaining</div>
      </div>

      <button class="start-btn" @click="startLearning">
        <i class="el-icon-video-play"></i> Start Learning
      </button>

      <button class="wrong-btn" @click="$router.push('/wrong-book')">
        <i class="el-icon-document"></i> Wrong Words ({{ wrongCount }})
      </button>
    </div>

    <!-- Learning View -->
    <div v-else class="learning-section">
      <!-- Progress Bar -->
      <div class="learn-progress">
        <span>Progress: {{ learnedToday }}/{{ dailyGoal }}</span>
        <span>{{ remainingCount }} left</span>
      </div>

      <!-- Word Card -->
      <div class="word-card">
        <div class="word-text">{{ currentWord.word }}</div>
        <div v-if="showMeaning" class="meaning-text">{{ currentWord.meaning }}</div>
        <div v-if="showMeaning && currentWord.phonetic" class="phonetic-text">{{ currentWord.phonetic }}</div>

        <button v-if="!showMeaning" class="reveal-btn" @click="showMeaning = true">
          Show Meaning
        </button>

        <!-- Mode: Flashcard -->
        <template v-if="currentMode === 'flashcard' && showMeaning">
          <div class="action-row">
            <button class="unk-btn" @click="markUnknown">Don't Know</button>
            <button class="knw-btn" @click="markKnown">Know It</button>
          </div>
        </template>

        <!-- Mode: Fill Blank -->
        <template v-if="currentMode === 'fillBlank'">
          <div class="hint-text">
            Hint: first letter <span class="hl">{{ currentWord.word.charAt(0).toUpperCase() }}</span>,
            {{ currentWord.word.length }} letters
          </div>
          <input
            v-model="fillBlankAnswer"
            class="answer-input"
            :placeholder="fillBlankChecked ? '' : 'Type the word'"
            :disabled="fillBlankChecked"
            @keyup.enter="checkFillBlank"
          />
          <div v-if="fillBlankChecked" class="feedback" :class="fillBlankCorrect ? 'ok' : 'err'">
            {{ fillBlankCorrect ? 'Correct!' : 'Answer: ' + currentWord.word }}
          </div>
          <button class="check-btn" @click="checkFillBlank">
            {{ fillBlankChecked ? 'Next' : 'Check' }}
          </button>
        </template>

        <!-- Mode: Spelling -->
        <template v-if="currentMode === 'spelling'">
          <div class="meaning-display">{{ currentWord.meaning }}</div>
          <input
            v-model="spellingAnswer"
            class="answer-input"
            :placeholder="spellingChecked ? '' : 'Spell the word'"
            :disabled="spellingChecked"
            @keyup.enter="checkSpelling"
          />
          <div v-if="spellingChecked" class="feedback" :class="spellingCorrect ? 'ok' : 'err'">
            {{ spellingCorrect ? 'Correct!' : 'Answer: ' + currentWord.word }}
          </div>
          <button class="check-btn" @click="checkSpelling">
            {{ spellingChecked ? 'Next' : 'Check' }}
          </button>
        </template>
      </div>

      <!-- Mode Switcher -->
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

    <!-- Settings Modal -->
    <div v-if="showSettings" class="modal-mask" @click="showSettings = false">
      <div class="modal" @click.stop>
        <div class="modal-hd">
          <span>Settings</span>
          <button class="x-btn" @click="showSettings = false">x</button>
        </div>
        <div class="modal-bd">
          <div class="field">
            <label>Daily Goal (words)</label>
            <input v-model.number="settings.dailyGoal" type="number" min="1" max="200" />
          </div>
          <div class="field">
            <label>Reminder Time</label>
            <input v-model="settings.reminderTime" type="time" />
          </div>
          <div class="field">
            <label>Rest (minutes)</label>
            <input v-model.number="settings.restDuration" type="number" min="1" max="60" />
          </div>
        </div>
        <div class="modal-ft">
          <button class="cancel-btn" @click="showSettings = false">Cancel</button>
          <button class="save-btn" @click="saveSettings">Save</button>
        </div>
      </div>
    </div>

    <!-- Toast -->
    <div v-if="toast.show" class="toast-msg">{{ toast.message }}</div>

    <!-- Fireworks -->
    <div v-if="showFireworks" class="fireworks">
      <div class="congrats">Goal Achieved!</div>
      <button class="close-fireworks" @click="showFireworks = false">x</button>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref, computed, onMounted } from 'vue';
import { ElMessage } from 'element-plus';
import { get, post } from '@/api/index';

interface Word { id: number; word: string; phonetic: string; meaning: string; }
interface WordBook { id: number; name: string; category: string; wordCount: number; }

const modes = [
  { value: 'flashcard', label: 'Flashcard' },
  { value: 'fillBlank', label: 'Fill Blank' },
  { value: 'spelling', label: 'Spelling' }
];

const books = ref<WordBook[]>([]);
const selectedBookId = ref<number>(2);
const showLearning = ref(false);
const showSettings = ref(false);
const settings = ref({ dailyGoal: 20, reminderTime: '', restDuration: 2 });

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
let goalAchieved = false;

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
  showToast('Settings saved');
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
      ElMessage.warning('No words available');
    }
  } catch (e) { ElMessage.error('Failed to load words'); }
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

const checkFillBlank = () => {
  if (fillBlankChecked.value) { nextWord(); return; }
  fillBlankChecked.value = true;
  fillBlankCorrect.value = fillBlankAnswer.value.trim().toLowerCase() === currentWord.value.word.toLowerCase();
  showToast(fillBlankCorrect.value ? 'Correct!' : 'Answer: ' + currentWord.value.word);
};

const checkSpelling = () => {
  if (spellingChecked.value) { nextWord(); return; }
  spellingChecked.value = true;
  spellingCorrect.value = spellingAnswer.value.trim().toLowerCase() === currentWord.value.word.toLowerCase();
  showToast(spellingCorrect.value ? 'Correct!' : 'Answer: ' + currentWord.value.word);
};

const markKnown = async () => {
  const justAchieved = !goalAchieved && learnedToday.value >= dailyGoal.value - 1;
  learnedToday.value++;
  saveProgress();
  if (justAchieved) {
    showFireworks.value = true;
    setTimeout(() => { showFireworks.value = false; }, 6000);
  }
  if (currentWord.value.id) {
    try { await post('/words/correct', null, { params: { wordId: currentWord.value.id, bookId: selectedBookId.value } }); } catch (e) {}
  }
  nextWord();
};

const markUnknown = async () => {
  wrongCount.value++;
  if (currentWord.value.id) {
    try { await post('/words/wrong', null, { params: { wordId: currentWord.value.id, bookId: selectedBookId.value } }); } catch (e) {}
  }
  nextWord();
};

const nextWord = () => {
  resetState();
  if (currentIndex.value < wordList.value.length - 1) {
    currentIndex.value++;
  } else {
    showLearning.value = false;
    ElMessage.success('Session complete!');
  }
};

const showToast = (msg: string) => {
  toast.value = { show: true, message: msg };
  setTimeout(() => { toast.value.show = false; }, 2500);
};

onMounted(() => { loadBooks(); loadProgress(); });
</script>

<style scoped>
.word-page {
  min-height: 100%;
  padding: 0;
  background: transparent;
}

/* Top Bar */
.top-bar {
  display: flex;
  align-items: center;
  justify-content: space-between;
  padding: 12px 16px;
  background: linear-gradient(135deg, #667eea, #764ba2);
  border-radius: 12px;
  margin-bottom: 16px;
}
.app-title { font-size: 18px; font-weight: bold; color: white; }
.top-bar-right { display: flex; gap: 8px; }
.icon-btn { width: 36px; height: 36px; border-radius: 50%; background: rgba(255,255,255,0.2); border: none; color: white; font-size: 16px; cursor: pointer; display: flex; align-items: center; justify-content: center; }
.icon-btn:hover { background: rgba(255,255,255,0.35); }

/* Welcome */
.welcome-section { padding: 8px 0; }
.progress-card { background: linear-gradient(135deg, #667eea, #764ba2); border-radius: 16px; padding: 20px; color: white; margin-bottom: 16px; }
.progress-label { font-size: 13px; opacity: 0.9; margin-bottom: 6px; }
.progress-row { font-size: 32px; font-weight: bold; margin-bottom: 10px; }
.sep { opacity: 0.6; margin: 0 4px; }
.progress-bar-bg { height: 6px; background: rgba(255,255,255,0.3); border-radius: 3px; overflow: hidden; margin-bottom: 6px; }
.progress-bar-fill { height: 100%; background: white; border-radius: 3px; transition: width 0.4s; }
.remaining { font-size: 12px; opacity: 0.8; }
.start-btn { width: 100%; padding: 14px; background: linear-gradient(135deg, #10b981, #059669); color: white; border: none; border-radius: 12px; font-size: 16px; font-weight: bold; cursor: pointer; margin-bottom: 10px; display: flex; align-items: center; justify-content: center; gap: 6px; }
.wrong-btn { width: 100%; padding: 12px; background: white; color: #667eea; border: 2px solid #667eea; border-radius: 12px; font-size: 14px; font-weight: 600; cursor: pointer; display: flex; align-items: center; justify-content: center; gap: 6px; }

/* Learning */
.learn-progress { display: flex; justify-content: space-between; font-size: 13px; color: #909399; margin-bottom: 12px; }
.word-card { background: white; border-radius: 16px; padding: 28px 20px; box-shadow: 0 2px 12px rgba(0,0,0,0.08); text-align: center; margin-bottom: 12px; }
.word-text { font-size: 44px; font-weight: bold; color: #303133; margin-bottom: 12px; }
.meaning-text { font-size: 20px; color: #667eea; margin-bottom: 6px; }
.phonetic-text { font-size: 14px; color: #909399; margin-bottom: 16px; }
.reveal-btn { padding: 10px 28px; background: linear-gradient(135deg, #667eea, #764ba2); color: white; border: none; border-radius: 8px; font-size: 14px; cursor: pointer; }
.action-row { display: flex; gap: 12px; margin-top: 12px; }
.unk-btn, .knw-btn { flex: 1; padding: 12px; border: none; border-radius: 10px; font-size: 15px; font-weight: bold; cursor: pointer; }
.unk-btn { background: #ef4444; color: white; }
.knw-btn { background: #10b981; color: white; }
.hint-text { font-size: 13px; color: #909399; margin-bottom: 10px; }
.hl { color: #667eea; font-weight: bold; }
.meaning-display { font-size: 18px; color: #303133; margin-bottom: 10px; }
.answer-input { width: 100%; padding: 12px; font-size: 18px; border: 2px solid #e4e7ed; border-radius: 10px; text-align: center; outline: none; margin-bottom: 8px; box-sizing: border-box; }
.answer-input:focus { border-color: #667eea; }
.feedback { padding: 8px; border-radius: 6px; font-size: 13px; margin-bottom: 8px; }
.feedback.ok { background: #d1fae5; color: #065f46; }
.feedback.err { background: #fee2e2; color: #991b1b; }
.check-btn { padding: 10px 28px; background: linear-gradient(135deg, #667eea, #764ba2); color: white; border: none; border-radius: 8px; font-size: 14px; cursor: pointer; }

/* Mode Bar */
.mode-bar { display: flex; gap: 6px; background: white; border-radius: 10px; padding: 4px; box-shadow: 0 1px 4px rgba(0,0,0,0.06); }
.mode-btn { flex: 1; padding: 7px; border: none; background: transparent; border-radius: 7px; font-size: 12px; color: #909399; cursor: pointer; transition: all 0.2s; }
.mode-btn.active { background: #667eea; color: white; }

/* Modal */
.modal-mask { position: fixed; inset: 0; background: rgba(0,0,0,0.5); display: flex; align-items: center; justify-content: center; z-index: 200; }
.modal { background: white; border-radius: 16px; width: 90%; max-width: 340px; overflow: hidden; }
.modal-hd { display: flex; justify-content: space-between; align-items: center; padding: 16px 20px; border-bottom: 1px solid #f0f0f0; font-size: 16px; font-weight: bold; color: #303133; }
.x-btn { width: 28px; height: 28px; border-radius: 50%; background: #f5f5f5; border: none; cursor: pointer; font-size: 14px; color: #909399; }
.modal-bd { padding: 16px 20px; }
.field { margin-bottom: 14px; }
.field label { display: block; font-size: 13px; color: #606266; margin-bottom: 5px; }
.field input { width: 100%; padding: 8px 10px; border: 1px solid #dcdfe6; border-radius: 6px; font-size: 14px; box-sizing: border-box; }
.modal-ft { display: flex; gap: 10px; padding: 12px 20px; border-top: 1px solid #f0f0f0; }
.cancel-btn, .save-btn { flex: 1; padding: 9px; border: none; border-radius: 8px; font-size: 14px; cursor: pointer; }
.cancel-btn { background: #f5f5f5; color: #606266; }
.save-btn { background: linear-gradient(135deg, #667eea, #764ba2); color: white; }

/* Toast */
.toast-msg { position: fixed; bottom: 40px; left: 50%; transform: translateX(-50%); background: rgba(0,0,0,0.75); color: white; padding: 10px 22px; border-radius: 8px; font-size: 14px; z-index: 300; }

/* Fireworks */
.fireworks { position: fixed; inset: 0; background: rgba(0,0,0,0.7); display: flex; align-items: center; justify-content: center; z-index: 300; }
.congrats { font-size: 32px; color: white; font-weight: bold; animation: pop 1s infinite; }
@keyframes pop { 0%,100%{transform:scale(1)} 50%{transform:scale(1.1)} }
.close-fireworks { position: absolute; top: 30px; right: 30px; width: 36px; height: 36px; border-radius: 50%; background: rgba(255,255,255,0.2); border: 2px solid white; color: white; font-size: 18px; cursor: pointer; }
</style>
