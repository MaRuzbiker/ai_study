<template>
  <div class="resume-template">
    <el-card shadow="never">
      <template #header>
        <div class="card-header">
          <span>选择简历模板</span>
          <el-button text @click="$router.back()">
            <el-icon><ArrowLeft /></el-icon>返回
          </el-button>
        </div>
      </template>

      <!-- 分类筛选 -->
      <div class="filter-bar">
        <el-radio-group v-model="selectedCategory" size="default">
          <el-radio-button label="">全部</el-radio-button>
          <el-radio-button label="简洁">简洁</el-radio-button>
          <el-radio-button label="专业">专业</el-radio-button>
          <el-radio-button label="创意">创意</el-radio-button>
          <el-radio-button label="技术">技术</el-radio-button>
          <el-radio-button label="简约">简约</el-radio-button>
        </el-radio-group>
      </div>

      <!-- 加载中 -->
      <div v-if="loading" style="text-align: center; padding: 60px;">
        <el-icon class="is-loading" style="font-size: 32px;"><Loading /></el-icon>
      </div>

      <!-- 模板卡片列表 -->
      <el-row v-else :gutter="20">
        <!-- 模板1：经典蓝色 -->
        <el-col :span="8" style="margin-bottom: 20px;" v-if="!selectedCategory || selectedCategory === '简洁'">
          <div class="template-card" :class="{ active: selectedId === 1 }" @click="selectedId = 1">
            <div class="template-mini-preview">
              <div class="mini-screen">
                <div class="ms-hdr ms-hdr-blue"></div>
                <div class="ms-line"></div>
                <div class="ms-line ms-w60"></div>
                <div class="ms-line ms-w80"></div>
                <div class="ms-sec-title ms-blue"></div>
                <div class="ms-line"></div>
                <div class="ms-line ms-w50"></div>
                <div class="ms-sec-title ms-blue"></div>
                <div class="ms-line"></div>
                <div class="ms-line ms-w70"></div>
              </div>
            </div>
            <div class="template-footer">
              <div>
                <div class="template-name">经典蓝色</div>
                <div class="template-meta">
                  <el-tag size="small" type="info">简洁</el-tag>
                </div>
              </div>
              <el-icon v-if="selectedId === 1" color="#409eff" size="18"><Select /></el-icon>
            </div>
          </div>
        </el-col>

        <!-- 模板2：专业深灰 -->
        <el-col :span="8" style="margin-bottom: 20px;" v-if="!selectedCategory || selectedCategory === '专业'">
          <div class="template-card" :class="{ active: selectedId === 2 }" @click="selectedId = 2">
            <div class="template-mini-preview">
              <div class="mini-screen ms-two-col">
                <div class="ms-sidebar-gray"></div>
                <div class="ms-main">
                  <div class="ms-hdr ms-hdr-gray"></div>
                  <div class="ms-line ms-w40"></div>
                  <div class="ms-sec-title ms-orange"></div>
                  <div class="ms-line ms-w60"></div>
                  <div class="ms-sec-title ms-orange"></div>
                  <div class="ms-line ms-w50"></div>
                </div>
              </div>
            </div>
            <div class="template-footer">
              <div>
                <div class="template-name">专业深灰</div>
                <div class="template-meta">
                  <el-tag size="small" type="info">专业</el-tag>
                </div>
              </div>
              <el-icon v-if="selectedId === 2" color="#409eff" size="18"><Select /></el-icon>
            </div>
          </div>
        </el-col>

        <!-- 模板3：清新绿色（单栏时间线） -->
        <el-col :span="8" style="margin-bottom: 20px;" v-if="!selectedCategory || selectedCategory === '创意'">
          <div class="template-card" :class="{ active: selectedId === 3 }" @click="selectedId = 3">
            <div class="template-mini-preview">
              <div class="mini-screen">
                <div class="ms-hdr ms-hdr-green">
                  <div class="ms-name-dot"></div>
                  <div class="ms-name-line"></div>
                  <div class="ms-contact-strip"></div>
                </div>
                <div class="ms-timeline">
                  <div class="ms-tl-dot green"></div>
                  <div class="ms-tl-content">
                    <div class="ms-tl-title"></div>
                    <div class="ms-tl-sub"></div>
                  </div>
                </div>
                <div class="ms-timeline">
                  <div class="ms-tl-dot green"></div>
                  <div class="ms-tl-content">
                    <div class="ms-tl-title"></div>
                    <div class="ms-tl-sub"></div>
                  </div>
                </div>
                <div class="ms-timeline">
                  <div class="ms-tl-dot green"></div>
                  <div class="ms-tl-content">
                    <div class="ms-tl-title"></div>
                    <div class="ms-tl-sub"></div>
                  </div>
                </div>
              </div>
            </div>
            <div class="template-footer">
              <div>
                <div class="template-name">清新绿色</div>
                <div class="template-meta">
                  <el-tag size="small" type="info">创意</el-tag>
                </div>
              </div>
              <el-icon v-if="selectedId === 3" color="#409eff" size="18"><Select /></el-icon>
            </div>
          </div>
        </el-col>

        <!-- 模板4：技术风格 -->
        <el-col :span="8" style="margin-bottom: 20px;" v-if="!selectedCategory || selectedCategory === '技术'">
          <div class="template-card" :class="{ active: selectedId === 4 }" @click="selectedId = 4">
            <div class="template-mini-preview">
              <div class="mini-screen">
                <div class="ms-hdr ms-hdr-dark"></div>
                <div class="ms-tags">
                  <div class="ms-tag"></div>
                  <div class="ms-tag"></div>
                  <div class="ms-tag"></div>
                </div>
                <div class="ms-sec-title ms-dark"></div>
                <div class="ms-line"></div>
                <div class="ms-line ms-w50"></div>
                <div class="ms-sec-title ms-dark"></div>
                <div class="ms-line"></div>
              </div>
            </div>
            <div class="template-footer">
              <div>
                <div class="template-name">技术风格</div>
                <div class="template-meta">
                  <el-tag size="small" type="info">技术</el-tag>
                </div>
              </div>
              <el-icon v-if="selectedId === 4" color="#409eff" size="18"><Select /></el-icon>
            </div>
          </div>
        </el-col>

        <!-- 模板5：简约白色 -->
        <el-col :span="8" style="margin-bottom: 20px;" v-if="!selectedCategory || selectedCategory === '简约'">
          <div class="template-card" :class="{ active: selectedId === 5 }" @click="selectedId = 5">
            <div class="template-mini-preview">
              <div class="mini-screen">
                <div class="ms-name-sm"></div>
                <div class="ms-contact-sm"></div>
                <div class="ms-divider"></div>
                <div class="ms-label"></div>
                <div class="ms-line"></div>
                <div class="ms-line ms-w50"></div>
                <div class="ms-divider sm"></div>
                <div class="ms-label"></div>
                <div class="ms-line"></div>
              </div>
            </div>
            <div class="template-footer">
              <div>
                <div class="template-name">简约白色</div>
                <div class="template-meta">
                  <el-tag size="small" type="info">简约</el-tag>
                </div>
              </div>
              <el-icon v-if="selectedId === 5" color="#409eff" size="18"><Select /></el-icon>
            </div>
          </div>
        </el-col>
      </el-row>

      <!-- 确认选择 -->
      <div v-if="selectedId" class="confirm-bar">
        <el-button type="primary" size="large" @click="confirmSelect">
          使用此模板创建简历 →
        </el-button>
      </div>
    </el-card>
  </div>
</template>

<script lang="ts" setup>
import { ref, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import { ElMessage } from 'element-plus'
import { ArrowLeft, Loading, Select } from '@element-plus/icons-vue'

const router = useRouter()
const selectedCategory = ref('')
const selectedId = ref<number | null>(null)
const loading = ref(false)

const confirmSelect = () => {
  if (!selectedId.value) return
  router.push({ path: '/resume/edit', query: { templateId: String(selectedId.value) } })
}

onMounted(() => { selectedId.value = 1; loading.value = false })
</script>

<style scoped>
.card-header { display: flex; justify-content: space-between; align-items: center; }
.filter-bar { margin-bottom: 20px; }

/* 模板卡片 */
.template-card {
  border: 2px solid #e8e8e8;
  border-radius: 12px;
  overflow: hidden;
  cursor: pointer;
  transition: all 0.3s;
  background: #fff;
}
.template-card:hover {
  transform: translateY(-3px);
  box-shadow: 0 8px 24px rgba(0,0,0,0.10);
  border-color: #b0c8ff;
}
.template-card.active {
  border-color: #409eff;
  box-shadow: 0 0 0 3px rgba(64,158,255,0.15);
}

/* 微缩预览区 */
.template-mini-preview {
  background: #f0f2f5;
  padding: 16px;
  display: flex;
  justify-content: center;
  align-items: center;
  height: 220px;
}
.mini-screen {
  background: #fff;
  width: 160px;
  height: 220px;
  border-radius: 4px;
  box-shadow: 0 2px 12px rgba(0,0,0,0.15);
  overflow: hidden;
  padding: 10px 12px;
  font-size: 4px;
  display: flex;
  flex-direction: column;
  gap: 5px;
}

/* 经典蓝色 */
.ms-hdr-blue { background: linear-gradient(135deg, #1a73e8, #4285f4); height: 50px; border-radius: 3px; margin-bottom: 4px; }

/* 深灰侧栏 */
.ms-two-col { flex-direction: row; padding: 0; }
.ms-sidebar-gray { width: 55px; background: #263238; flex-shrink: 0; }
.ms-main { flex: 1; padding: 10px 8px; display: flex; flex-direction: column; gap: 5px; }
.ms-hdr-gray { height: 40px; background: #37474f; border-radius: 0 3px 0 0; }

/* 清新绿色 */
.ms-hdr-green { background: linear-gradient(135deg, #2e7d32, #43a047); height: 48px; border-radius: 3px; margin-bottom: 4px; display: flex; flex-direction: column; justify-content: center; padding: 6px 10px; gap: 4px; }
.ms-name-dot { width: 24px; height: 24px; border-radius: 50%; background: rgba(255,255,255,0.2); border: 2px solid rgba(255,255,255,0.4); }
.ms-name-line { height: 5px; width: 60%; background: rgba(255,255,255,0.8); border-radius: 2px; }
.ms-contact-strip { height: 4px; width: 80%; background: rgba(255,255,255,0.4); border-radius: 2px; }
.ms-timeline { display: flex; gap: 6px; align-items: flex-start; }
.ms-tl-dot { width: 8px; height: 8px; border-radius: 50%; flex-shrink: 0; margin-top: 2px; }
.ms-tl-dot.green { background: #43a047; border: 1.5px solid #a5d6a7; }
.ms-tl-content { flex: 1; display: flex; flex-direction: column; gap: 2px; }
.ms-tl-title { height: 5px; background: #bbb; border-radius: 2px; width: 70%; }
.ms-tl-sub { height: 3.5px; background: #ddd; border-radius: 2px; }

/* 技术风格 */
.ms-hdr-dark { background: linear-gradient(135deg, #0d47a1, #1565c0); height: 48px; border-radius: 3px; margin-bottom: 4px; }
.ms-tags { display: flex; gap: 3px; margin-bottom: 4px; }
.ms-tag { height: 8px; width: 28px; background: rgba(255,255,255,0.2); border: 1px solid rgba(255,255,255,0.3); border-radius: 3px; }

/* 简约白色 */
.ms-name-sm { height: 8px; width: 65%; background: #111; border-radius: 2px; margin-bottom: 3px; }
.ms-contact-sm { height: 3px; width: 80%; background: #bbb; border-radius: 2px; margin-bottom: 4px; }
.ms-divider { height: 1px; background: #e0e0e0; margin: 2px 0; }
.ms-divider.sm { margin: 4px 0; }
.ms-label { height: 4px; width: 30%; background: #999; border-radius: 2px; margin-bottom: 2px; }

/* 通用 */
.ms-line { height: 3.5px; background: #ddd; border-radius: 2px; }
.ms-w60 { width: 60%; }
.ms-w50 { width: 50%; }
.ms-w70 { width: 70%; }
.ms-w80 { width: 80%; }
.ms-w40 { width: 40%; }
.ms-sec-title { height: 5px; width: 35%; border-radius: 2px; margin: 2px 0; }
.ms-blue { background: #1a73e8; }
.ms-orange { background: #ff6b35; }
.ms-dark { background: #1976d2; }

/* 底部 */
.template-footer {
  padding: 12px 14px;
  display: flex;
  justify-content: space-between;
  align-items: center;
  background: #fff;
}
.template-name { font-size: 14px; font-weight: 600; color: #333; margin-bottom: 3px; }
.template-meta { display: flex; align-items: center; gap: 6px; }

/* 确认栏 */
.confirm-bar {
  margin-top: 20px;
  padding-top: 16px;
  border-top: 1px solid #eee;
  text-align: center;
}
</style>
