<template>
  <div class="wrong-book-page">
    <!-- 顶部 -->
    <header class="page-header">
      <button class="back-btn" @click="goBack">
        <i class="el-icon-arrow-left"></i>
      </button>
      <h1 class="page-title">{{ pageTitle }}</h1>
      <button v-if="!viewingOther" class="clear-btn" @click="confirmClear" :disabled="wrongWords.length === 0">
        <i class="el-icon-delete"></i> 清空
      </button>
      <div v-else style="width: 60px;"></div>
    </header>

    <!-- 词书筛选 -->
    <div class="filter-section">
      <div class="book-tabs">
        <button
          v-for="book in books"
          :key="book.id"
          class="book-tab"
          :class="{ active: selectedBookId === book.id }"
          @click="switchBook(book.id)"
        >
          {{ book.name }}
        </button>
      </div>
    </div>

    <!-- 统计 -->
    <div v-if="wrongWords.length > 0" class="stats-bar">
      <div class="stat-item">
        <span class="stat-num">{{ wrongWords.length }}</span>
        <span class="stat-label">错词数</span>
      </div>
      <div class="stat-item">
        <span class="stat-num">{{ totalWrong }}</span>
        <span class="stat-label">总错误次数</span>
      </div>
    </div>

    <!-- 错词列表 -->
    <div v-if="wrongWords.length > 0" class="word-list">
      <div
        v-for="item in wrongWords"
        :key="item.id"
        class="word-card"
        @click="showDetail(item)"
      >
        <div class="word-main">
          <div class="word-text">{{ item.word }}</div>
          <div class="word-phonetic">{{ item.phonetic }}</div>
          <div class="word-meaning">{{ item.meaning }}</div>
        </div>
        <div class="word-stats">
          <div class="stat-badge wrong">
            <span class="num">{{ item.wrongCount }}</span>
            <span class="label">错</span>
          </div>
          <div class="stat-badge correct">
            <span class="num">{{ item.correctCount }}</span>
            <span class="label">对</span>
          </div>
        </div>
      </div>
    </div>

    <!-- 空状态 -->
    <div v-else class="empty-state">
      <div class="empty-icon">{{ viewingOther ? '📚' : '🎉' }}</div>
      <div class="empty-text">{{ viewingOther ? `${targetNickname} 暂无错词` : '太棒了！暂无错词' }}</div>
      <button v-if="!viewingOther" class="start-learn-btn" @click="$router.push('/words')">
        去背单词
      </button>
    </div>

    <!-- 详情弹窗 -->
    <div v-if="showDetailModal" class="modal-overlay" @click="showDetailModal = false">
      <div class="detail-modal" @click.stop>
        <div class="detail-word">{{ currentItem.word }}</div>
        <div class="detail-phonetic">{{ currentItem.phonetic }}</div>
        <div class="detail-meaning">{{ currentItem.meaning }}</div>

        <div class="detail-stats">
          <div class="detail-stat">
            <span class="ds-num">{{ currentItem.wrongCount }}</span>
            <span class="ds-label">错误</span>
          </div>
          <div class="detail-stat">
            <span class="ds-num">{{ currentItem.correctCount }}</span>
            <span class="ds-label">正确</span>
          </div>
        </div>

        <div v-if="!viewingOther" class="detail-actions">
          <button class="master-btn" @click="markMastered">
            <i class="el-icon-check"></i> 已掌握
          </button>
        </div>

        <button class="close-detail-btn" @click="showDetailModal = false">关闭</button>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref, computed, onMounted } from 'vue';
import { useRoute, useRouter } from 'vue-router';
import { ElMessage, ElMessageBox } from 'element-plus';
import { get, del } from '@/api/index';

interface WordBook { id: number; name: string; category: string; wordCount: number; }
interface WrongWord {
  id: number;
  wordId: number;
  word: string;
  phonetic: string;
  meaning: string;
  wrongCount: number;
  correctCount: number;
  status: string;
}

const route = useRoute();
const router = useRouter();

const books = ref<WordBook[]>([]);
const selectedBookId = ref<number>(2);
const wrongWords = ref<WrongWord[]>([]);
const showDetailModal = ref(false);
const currentItem = ref<WrongWord>({} as WrongWord);
const totalWrong = ref(0);

// 查看其他用户错词本
const targetUserId = computed(() => route.query.userId ? Number(route.query.userId) : null);
const targetNickname = computed(() => route.query.nickname as string || '');
const viewingOther = computed(() => !!targetUserId.value);
const pageTitle = computed(() => viewingOther.value ? `${targetNickname.value} 的错词本` : '我的错词本');

const goBack = () => {
  if (viewingOther.value) {
    router.push('/deskmate');
  } else {
    router.push('/words');
  }
};

const loadBooks = async () => {
  try {
    const res = await get<WordBook[]>('/words/books');
    if (res.code === 0) {
      books.value = res.data || [];
      // 默认选中 CET-6
      const cet6 = books.value.find(b => b.name.includes('六级') || b.id === 2);
      selectedBookId.value = cet6?.id || 2;
      loadWrongWords();
    }
  } catch (e) { console.error(e); }
};

const loadWrongWords = async () => {
  if (!selectedBookId.value) return;
  try {
    let res;
    if (viewingOther.value && targetUserId.value) {
      // 查看其他用户的错词本
      res = await get<WrongWord[]>(`/deskmate/wrong-words/${targetUserId.value}`, { bookId: selectedBookId.value });
    } else {
      // 查看自己的错词本
      res = await get<WrongWord[]>('/words/wrong-book/' + selectedBookId.value);
    }
    if (res.code === 0) {
      wrongWords.value = res.data || [];
      totalWrong.value = wrongWords.value.reduce((sum, w) => sum + (w.wrongCount || 0), 0);
    }
  } catch (e) { console.error(e); }
};

const switchBook = (bookId: number) => {
  selectedBookId.value = bookId;
  loadWrongWords();
};

const showDetail = (item: WrongWord) => {
  currentItem.value = item;
  showDetailModal.value = true;
};

const markMastered = async () => {
  try {
    await del('/words/wrong-book/' + currentItem.value.wordId);
    ElMessage.success('恭喜掌握了这个单词！');
    showDetailModal.value = false;
    loadWrongWords();
  } catch (e) { ElMessage.error('操作失败'); }
};

const confirmClear = async () => {
  if (wrongWords.value.length === 0) return;
  try {
    await ElMessageBox.confirm('确定要清空当前词书的错词吗？', '清空确认', {
      confirmButtonText: '确定清空',
      cancelButtonText: '取消',
      type: 'warning'
    });
    await del('/words/wrong-book/clear/' + selectedBookId.value);
    ElMessage.success('已清空错词');
    loadWrongWords();
  } catch (e) { /* 取消 */ }
};

onMounted(() => { loadBooks(); });
</script>

<style scoped>
.wrong-book-page { min-height: 100%; background: transparent; padding-bottom: 20px; }

/* 顶部 */
.page-header { display: flex; align-items: center; justify-content: space-between; padding: 12px 0; margin-bottom: 12px; }
.back-btn { width: 36px; height: 36px; border-radius: 50%; background: #f5f5f5; border: none; font-size: 16px; cursor: pointer; display: flex; align-items: center; justify-content: center; }
.page-title { font-size: 18px; font-weight: bold; color: #303133; }
.clear-btn { padding: 8px 14px; background: #fef0f0; color: #f56c6c; border: none; border-radius: 6px; font-size: 13px; cursor: pointer; display: flex; align-items: center; gap: 4px; }
.clear-btn:disabled { opacity: 0.5; cursor: not-allowed; }

/* 词书切换 */
.filter-section { margin-bottom: 12px; }
.book-tabs { display: flex; gap: 8px; overflow-x: auto; padding-bottom: 4px; }
.book-tab { padding: 6px 14px; background: #f5f5f5; border: 2px solid transparent; border-radius: 16px; color: #606266; font-size: 13px; white-space: nowrap; cursor: pointer; transition: all 0.2s; }
.book-tab.active { background: #667eea; color: white; border-color: #667eea; }

/* 统计 */
.stats-bar { display: flex; gap: 12px; margin-bottom: 12px; }
.stat-item { flex: 1; background: white; border-radius: 12px; padding: 12px; text-align: center; box-shadow: 0 1px 4px rgba(0,0,0,0.06); }
.stat-num { display: block; font-size: 24px; font-weight: bold; color: #667eea; }
.stat-label { font-size: 12px; color: #909399; }

/* 错词列表 */
.word-list { display: flex; flex-direction: column; gap: 10px; }
.word-card { background: white; border-radius: 12px; padding: 14px; display: flex; align-items: center; box-shadow: 0 1px 4px rgba(0,0,0,0.06); cursor: pointer; transition: transform 0.2s; }
.word-card:hover { transform: translateY(-2px); }
.word-main { flex: 1; }
.word-text { font-size: 18px; font-weight: bold; color: #303133; margin-bottom: 3px; }
.word-phonetic { font-size: 12px; color: #909399; margin-bottom: 3px; }
.word-meaning { font-size: 13px; color: #667eea; }
.word-stats { display: flex; gap: 6px; }
.stat-badge { padding: 6px 10px; border-radius: 8px; text-align: center; min-width: 36px; }
.stat-badge .num { display: block; font-size: 16px; font-weight: bold; }
.stat-badge .label { font-size: 10px; }
.stat-badge.wrong { background: #fef0f0; color: #f56c6c; }
.stat-badge.correct { background: #f0f9eb; color: #67c23a; }

/* 空状态 */
.empty-state { text-align: center; padding-top: 60px; }
.empty-icon { font-size: 60px; margin-bottom: 12px; }
.empty-text { font-size: 16px; color: #909399; margin-bottom: 20px; }
.start-learn-btn { padding: 12px 28px; background: linear-gradient(135deg, #667eea, #764ba2); color: white; border: none; border-radius: 12px; font-size: 14px; font-weight: bold; cursor: pointer; }

/* 详情弹窗 */
.modal-overlay { position: fixed; inset: 0; background: rgba(0,0,0,0.5); display: flex; align-items: center; justify-content: center; z-index: 200; }
.detail-modal { background: white; border-radius: 20px; padding: 28px; width: 90%; max-width: 340px; text-align: center; }
.detail-word { font-size: 36px; font-weight: bold; color: #303133; margin-bottom: 4px; }
.detail-phonetic { font-size: 14px; color: #909399; margin-bottom: 10px; }
.detail-meaning { font-size: 18px; color: #667eea; margin-bottom: 20px; line-height: 1.5; }
.detail-stats { display: flex; justify-content: center; gap: 28px; margin-bottom: 20px; }
.detail-stat { text-align: center; }
.ds-num { display: block; font-size: 24px; font-weight: bold; color: #667eea; }
.ds-label { font-size: 12px; color: #909399; }
.detail-actions { margin-bottom: 16px; }
.master-btn { width: 100%; padding: 12px; background: #67c23a; color: white; border: none; border-radius: 10px; font-size: 15px; font-weight: bold; cursor: pointer; display: flex; align-items: center; justify-content: center; gap: 6px; }
.close-detail-btn { width: 100%; padding: 10px; background: #f5f5f5; border: none; border-radius: 8px; color: #606266; font-size: 14px; cursor: pointer; }
</style>
