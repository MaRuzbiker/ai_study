<template>
  <div class="wrong-book-page">
    <div class="bg-gradient"></div>

    <header class="page-header">
      <button class="back-btn" @click="$router.push('/words')">
        <i class="el-icon-arrow-left"></i>
      </button>
      <h1 class="page-title">Wrong Words</h1>
      <button class="clear-btn" @click="confirmClear" :disabled="wrongWords.length === 0">
        <i class="el-icon-delete"></i> Clear
      </button>
    </header>

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

    <div v-if="wrongWords.length > 0" class="stats-bar">
      <div class="stat-item">
        <span class="stat-num">{{ wrongWords.length }}</span>
        <span class="stat-label">Wrong Words</span>
      </div>
      <div class="stat-item">
        <span class="stat-num">{{ totalWrong }}</span>
        <span class="stat-label">Total Wrong</span>
      </div>
    </div>

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
            <span class="label">Wrong</span>
          </div>
          <div class="stat-badge correct">
            <span class="num">{{ item.correctCount }}</span>
            <span class="label">Correct</span>
          </div>
        </div>
      </div>
    </div>

    <div v-else class="empty-state">
      <div class="empty-icon">🎉</div>
      <div class="empty-text">Great job! No wrong words.</div>
      <button class="start-learn-btn" @click="$router.push('/words')">
        Go Learn
      </button>
    </div>

    <div v-if="showDetailModal" class="modal-overlay" @click="showDetailModal = false">
      <div class="detail-modal" @click.stop>
        <div class="detail-word">{{ currentItem.word }}</div>
        <div class="detail-phonetic">{{ currentItem.phonetic }}</div>
        <div class="detail-meaning">{{ currentItem.meaning }}</div>

        <div class="detail-stats">
          <div class="detail-stat">
            <span class="ds-num">{{ currentItem.wrongCount }}</span>
            <span class="ds-label">Wrong</span>
          </div>
          <div class="detail-stat">
            <span class="ds-num">{{ currentItem.correctCount }}</span>
            <span class="ds-label">Correct</span>
          </div>
        </div>

        <div class="detail-actions">
          <button class="master-btn" @click="markMastered">
            <i class="el-icon-check"></i> Mastered
          </button>
          <button class="practice-btn" @click="practiceWord">
            <i class="el-icon-refresh"></i> Practice
          </button>
        </div>

        <button class="close-detail-btn" @click="showDetailModal = false">Close</button>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref, onMounted } from 'vue';
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

const books = ref<WordBook[]>([]);
const selectedBookId = ref<number>(2);
const wrongWords = ref<WrongWord[]>([]);
const showDetailModal = ref(false);
const currentItem = ref<WrongWord>({} as WrongWord);
const totalWrong = ref(0);

const loadBooks = async () => {
  try {
    const res = await get<WordBook[]>('/words/books');
    if (res.code === 0) {
      books.value = res.data || [];
      selectedBookId.value = 2;
      loadWrongWords();
    }
  } catch (e) { console.error(e); }
};

const loadWrongWords = async () => {
  if (!selectedBookId.value) return;
  try {
    const res = await get<WrongWord[]>('/words/wrong-book/' + selectedBookId.value);
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
    ElMessage.success('Word mastered!');
    showDetailModal.value = false;
    loadWrongWords();
  } catch (e) { ElMessage.error('Operation failed'); }
};

const practiceWord = () => {
  showDetailModal.value = false;
  ElMessage.info('Practice feature coming soon');
};

const confirmClear = async () => {
  if (wrongWords.value.length === 0) return;
  try {
    await ElMessageBox.confirm('Clear all wrong words for this book?', 'Confirm', {
      confirmButtonText: 'Clear',
      cancelButtonText: 'Cancel',
      type: 'warning'
    });
    await del('/words/wrong-book/clear/' + selectedBookId.value);
    ElMessage.success('Cleared!');
    loadWrongWords();
  } catch (e) { /* cancel */ }
};

onMounted(() => { loadBooks(); });
</script>

<style scoped>
.wrong-book-page { min-height: 100vh; background: linear-gradient(135deg, #667eea 0%, #764ba2 100%); padding-bottom: 40px; }
.bg-gradient { position: fixed; inset: 0; z-index: 0; }
.page-header { position: relative; z-index: 10; display: flex; align-items: center; justify-content: space-between; padding: 16px 20px; }
.back-btn { width: 40px; height: 40px; border-radius: 50%; background: rgba(255,255,255,0.2); border: none; color: white; font-size: 18px; cursor: pointer; display: flex; align-items: center; justify-content: center; }
.page-title { font-size: 20px; font-weight: bold; color: white; }
.clear-btn { padding: 8px 14px; background: rgba(255,255,255,0.2); border: none; border-radius: 20px; color: white; font-size: 13px; cursor: pointer; display: flex; align-items: center; gap: 6px; }
.clear-btn:disabled { opacity: 0.5; cursor: not-allowed; }
.filter-section { position: relative; z-index: 10; padding: 0 20px 16px; }
.book-tabs { display: flex; gap: 8px; overflow-x: auto; padding-bottom: 4px; }
.book-tab { padding: 8px 16px; background: rgba(255,255,255,0.2); border: 2px solid transparent; border-radius: 20px; color: white; font-size: 13px; white-space: nowrap; cursor: pointer; transition: all 0.2s; }
.book-tab.active { background: white; color: #667eea; border-color: white; }
.stats-bar { position: relative; z-index: 10; display: flex; gap: 16px; padding: 0 20px 16px; }
.stat-item { flex: 1; background: white; border-radius: 14px; padding: 16px; text-align: center; box-shadow: 0 2px 8px rgba(0,0,0,0.1); }
.stat-num { display: block; font-size: 28px; font-weight: bold; color: #667eea; }
.stat-label { font-size: 12px; color: #9ca3af; }
.word-list { position: relative; z-index: 10; padding: 0 20px; display: flex; flex-direction: column; gap: 12px; }
.word-card { background: white; border-radius: 16px; padding: 16px; display: flex; align-items: center; box-shadow: 0 2px 12px rgba(0,0,0,0.1); cursor: pointer; transition: transform 0.2s; }
.word-card:hover { transform: translateY(-2px); }
.word-main { flex: 1; }
.word-text { font-size: 20px; font-weight: bold; color: #1f2937; margin-bottom: 4px; }
.word-phonetic { font-size: 12px; color: #9ca3af; margin-bottom: 4px; }
.word-meaning { font-size: 14px; color: #667eea; }
.word-stats { display: flex; gap: 8px; }
.stat-badge { padding: 8px 12px; border-radius: 10px; text-align: center; min-width: 44px; }
.stat-badge .num { display: block; font-size: 18px; font-weight: bold; }
.stat-badge .label { font-size: 11px; }
.stat-badge.wrong { background: #fee2e2; color: #dc2626; }
.stat-badge.correct { background: #d1fae5; color: #059669; }
.empty-state { position: relative; z-index: 10; text-align: center; padding-top: 80px; }
.empty-icon { font-size: 80px; margin-bottom: 16px; }
.empty-text { font-size: 20px; color: white; font-weight: bold; margin-bottom: 24px; }
.start-learn-btn { padding: 14px 32px; background: white; color: #667eea; border: none; border-radius: 14px; font-size: 16px; font-weight: bold; cursor: pointer; }
.modal-overlay { position: fixed; inset: 0; background: rgba(0,0,0,0.5); display: flex; align-items: center; justify-content: center; z-index: 100; backdrop-filter: blur(4px); }
.detail-modal { background: white; border-radius: 24px; padding: 32px; width: 90%; max-width: 380px; text-align: center; }
.detail-word { font-size: 40px; font-weight: bold; color: #1f2937; margin-bottom: 6px; }
.detail-phonetic { font-size: 16px; color: #9ca3af; margin-bottom: 12px; }
.detail-meaning { font-size: 22px; color: #667eea; margin-bottom: 24px; }
.detail-stats { display: flex; justify-content: center; gap: 32px; margin-bottom: 24px; }
.detail-stat { text-align: center; }
.ds-num { display: block; font-size: 28px; font-weight: bold; color: #667eea; }
.ds-label { font-size: 12px; color: #9ca3af; }
.detail-actions { display: flex; gap: 12px; margin-bottom: 16px; }
.master-btn, .practice-btn { flex: 1; padding: 14px; border: none; border-radius: 12px; font-size: 15px; font-weight: bold; cursor: pointer; display: flex; align-items: center; justify-content: center; gap: 6px; }
.master-btn { background: #10b981; color: white; }
.practice-btn { background: #f3f4f6; color: #4b5563; }
.close-detail-btn { width: 100%; padding: 12px; background: #f3f4f6; border: none; border-radius: 10px; color: #6b7280; font-size: 14px; cursor: pointer; }
</style>
