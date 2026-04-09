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
        </el-radio-group>
      </div>

      <!-- 加载中 -->
      <div v-if="loading" style="text-align: center; padding: 60px;">
        <el-icon class="is-loading" style="font-size: 32px;"><Loading /></el-icon>
      </div>

      <!-- 5个内置模板 + 后端模板 -->
      <el-row v-else :gutter="20">
        <!-- 内置模板：经典蓝色 -->
        <el-col :span="8" style="margin-bottom: 20px;" v-if="!selectedCategory || selectedCategory === '简洁'">
          <div class="template-card" :class="{ active: selectedId === 1 }" @click="selectedId = 1">
            <div class="template-mini-preview">
              <div class="mini-resume tpl-mini-classic">
                <div class="mini-hdr"></div>
                <div class="mini-body-mini">
                  <div class="mini-line"></div>
                  <div class="mini-line short"></div>
                  <div class="mini-line"></div>
                </div>
              </div>
            </div>
            <div class="template-footer">
              <div>
                <div class="template-name">经典蓝色</div>
                <div class="template-meta">
                  <el-tag size="small" type="info">简洁</el-tag>
                  <span class="layout-tag">单栏式</span>
                </div>
              </div>
              <el-icon v-if="selectedId === 1" color="#409eff" size="18"><Select /></el-icon>
            </div>
          </div>
        </el-col>

        <!-- 内置模板：专业深灰 -->
        <el-col :span="8" style="margin-bottom: 20px;" v-if="!selectedCategory || selectedCategory === '专业'">
          <div class="template-card" :class="{ active: selectedId === 2 }" @click="selectedId = 2">
            <div class="template-mini-preview">
              <div class="mini-resume tpl-mini-pro">
                <div class="mini-sidebar-pro"></div>
                <div class="mini-main-pro">
                  <div class="mini-line"></div>
                  <div class="mini-line short"></div>
                  <div class="mini-line"></div>
                </div>
              </div>
            </div>
            <div class="template-footer">
              <div>
                <div class="template-name">专业深灰</div>
                <div class="template-meta">
                  <el-tag size="small" type="info">专业</el-tag>
                  <span class="layout-tag">左右栏</span>
                </div>
              </div>
              <el-icon v-if="selectedId === 2" color="#409eff" size="18"><Select /></el-icon>
            </div>
          </div>
        </el-col>

        <!-- 内置模板：清新绿色 -->
        <el-col :span="8" style="margin-bottom: 20px;" v-if="!selectedCategory || selectedCategory === '创意'">
          <div class="template-card" :class="{ active: selectedId === 3 }" @click="selectedId = 3">
            <div class="template-mini-preview">
              <div class="mini-resume tpl-mini-creative">
                <div class="mini-hdr-green"></div>
                <div class="mini-cards">
                  <div class="mini-card"></div>
                  <div class="mini-card"></div>
                </div>
              </div>
            </div>
            <div class="template-footer">
              <div>
                <div class="template-name">清新绿色</div>
                <div class="template-meta">
                  <el-tag size="small" type="info">创意</el-tag>
                  <span class="layout-tag">双栏卡片</span>
                </div>
              </div>
              <el-icon v-if="selectedId === 3" color="#409eff" size="18"><Select /></el-icon>
            </div>
          </div>
        </el-col>

        <!-- 内置模板：技术风格 -->
        <el-col :span="8" style="margin-bottom: 20px;" v-if="!selectedCategory || selectedCategory === '技术'">
          <div class="template-card" :class="{ active: selectedId === 4 }" @click="selectedId = 4">
            <div class="template-mini-preview">
              <div class="mini-resume tpl-mini-tech">
                <div class="mini-hdr-dark"></div>
                <div class="mini-body-mini">
                  <div class="mini-line-tech"></div>
                  <div class="mini-line-tech short"></div>
                  <div class="mini-line-tech"></div>
                </div>
              </div>
            </div>
            <div class="template-footer">
              <div>
                <div class="template-name">技术风格</div>
                <div class="template-meta">
                  <el-tag size="small" type="info">技术</el-tag>
                  <span class="layout-tag">等宽科技</span>
                </div>
              </div>
              <el-icon v-if="selectedId === 4" color="#409eff" size="18"><Select /></el-icon>
            </div>
          </div>
        </el-col>

        <!-- 内置模板：简约白色 -->
        <el-col :span="8" style="margin-bottom: 20px;" v-if="!selectedCategory || selectedCategory === '简洁'">
          <div class="template-card" :class="{ active: selectedId === 5 }" @click="selectedId = 5">
            <div class="template-mini-preview">
              <div class="mini-resume tpl-mini-minimal">
                <div class="mini-hdr-minimal"></div>
                <div class="mini-line-divider"></div>
                <div class="mini-body-minimal"></div>
              </div>
            </div>
            <div class="template-footer">
              <div>
                <div class="template-name">简约白色</div>
                <div class="template-meta">
                  <el-tag size="small" type="info">简洁</el-tag>
                  <span class="layout-tag">极简式</span>
                </div>
              </div>
              <el-icon v-if="selectedId === 5" color="#409eff" size="18"><Select /></el-icon>
            </div>
          </div>
        </el-col>

        <!-- 后端模板列表 -->
        <el-col v-for="tpl in filteredTemplates" :key="'remote-' + tpl.id" :span="8" style="margin-bottom: 20px;">
          <div class="template-card" :class="{ active: selectedId === tpl.id }" @click="selectedId = tpl.id">
            <div class="template-mini-preview">
              <div class="mini-resume">
                <div class="mini-header" :style="{ background: tplConfig(tpl).primary }">
                  <div class="mini-name"></div>
                  <div class="mini-contact"></div>
                </div>
                <div class="mini-body-mini">
                  <div class="mini-section" v-for="n in 3" :key="n">
                    <div class="mini-sec-title"></div>
                    <div class="mini-sec-line"></div>
                    <div class="mini-sec-line short"></div>
                  </div>
                </div>
              </div>
            </div>
            <div class="template-footer">
              <div>
                <div class="template-name">{{ tpl.name }}</div>
                <div class="template-meta">
                  <el-tag size="small" type="info">{{ tpl.category }}</el-tag>
                  <span class="layout-tag">{{ tplConfig(tpl).layoutLabel }}</span>
                </div>
              </div>
              <el-icon v-if="selectedId === tpl.id" color="#409eff" size="18"><Select /></el-icon>
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
import { ref, computed, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import { ElMessage } from 'element-plus'
import { ArrowLeft, Loading, Select } from '@element-plus/icons-vue'
import { getResumeTemplates } from '@/api/resume'

const router = useRouter()
const remoteTemplates = ref<any[]>([])
const selectedCategory = ref('')
const selectedId = ref<number | null>(null)
const loading = ref(true)

// 内置模板 ID 1-5 不在 filteredTemplates 中
const filteredTemplates = computed(() => {
  if (!selectedCategory.value) return remoteTemplates.value
  return remoteTemplates.value.filter(t => t.category === selectedCategory.value)
})

const tplConfig = (tpl: any) => {
  try {
    const config = typeof tpl.content === 'string' ? JSON.parse(tpl.content) : tpl.content
    const layoutMap: Record<string, string> = {
      'left-right': '左右栏', 'single': '单栏式', 'two-column': '双栏式',
      'compact': '紧凑式', 'minimal': '极简式'
    }
    return {
      primary: config.primaryColor || '#1a73e8',
      font: config.fontFamily || 'Microsoft YaHei',
      layout: config.layout || 'single',
      layoutLabel: layoutMap[config.layout] || config.layout || '单栏'
    }
  } catch {
    return { primary: '#1a73e8', font: 'Microsoft YaHei', layout: 'single', layoutLabel: '单栏' }
  }
}

const confirmSelect = () => {
  if (!selectedId.value) return
  // 传给 ResumeEdit 页面
  router.push({ path: '/resume/edit', query: { templateId: String(selectedId.value) } })
}

const loadTemplates = async () => {
  loading.value = true
  try {
    const res = await getResumeTemplates()
    if (res.code === 0) {
      remoteTemplates.value = res.data || []
      if (remoteTemplates.value.length) selectedId.value = remoteTemplates.value[0].id
      else selectedId.value = 1 // 默认选内置模板
    }
  } catch (e: any) {
    ElMessage.error(e.message || '加载模板失败')
    selectedId.value = 1
  } finally {
    loading.value = false
  }
}

onMounted(() => loadTemplates())
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

/* 微缩简历预览 */
.template-mini-preview {
  background: #f0f2f5;
  padding: 16px;
  display: flex;
  justify-content: center;
  align-items: center;
  height: 220px;
}
.mini-resume {
  background: #fff;
  width: 180px;
  height: 240px;
  border-radius: 4px;
  box-shadow: 0 2px 12px rgba(0,0,0,0.15);
  overflow: hidden;
  font-size: 5px;
  display: flex;
  flex-direction: column;
}

/* TplClassic 风格缩略图 */
.tpl-mini-classic .mini-hdr {
  background: linear-gradient(135deg, #1a73e8, #4285f4);
  height: 60px;
  padding: 10px 12px;
  color: #fff;
}
.mini-hdr .mini-name { height: 10px; width: 55%; background: rgba(255,255,255,0.9); border-radius: 2px; margin-bottom: 8px; }
.mini-hdr .mini-contact { height: 4px; width: 80%; background: rgba(255,255,255,0.6); border-radius: 2px; }
.mini-body-mini { flex: 1; padding: 10px 12px; }
.mini-line { height: 3px; background: #ddd; border-radius: 2px; margin-bottom: 6px; }
.mini-line.short { width: 65%; }

/* TplProfessional 风格缩略图 */
.tpl-mini-pro { flex-direction: row; }
.mini-sidebar-pro { width: 60px; background: #263238; padding: 10px 6px; flex-shrink: 0; }
.mini-sidebar-pro::before { content: ''; display: block; width: 36px; height: 36px; background: #ff6b35; border-radius: 50%; margin: 0 auto 8px; }
.mini-sidebar-pro::after { content: ''; display: block; width: 80%; height: 2px; background: #37474f; margin: 0 auto 6px; }
.mini-main-pro { flex: 1; padding: 10px 8px; }
.mini-section { margin-bottom: 10px; }
.mini-sec-title { height: 5px; background: #bbb; border-radius: 2px; margin-bottom: 4px; }
.mini-sec-line { height: 3px; background: #ddd; border-radius: 2px; margin-bottom: 3px; }
.mini-sec-line.short { width: 65%; }

/* TplCreative 风格缩略图 */
.tpl-mini-creative .mini-hdr-green { background: linear-gradient(135deg, #43a047, #66bb6a); height: 48px; }
.mini-cards { flex: 1; padding: 8px; display: grid; grid-template-columns: 1fr 1fr; gap: 6px; }
.mini-card { background: #f9f9f9; border-left: 2px solid #43a047; border-radius: 4px; height: 80px; }

/* TplTech 风格缩略图 */
.tpl-mini-tech .mini-hdr-dark { background: linear-gradient(135deg, #0d47a1, #1565c0); height: 55px; }
.tpl-mini-tech .mini-hdr-dark::after { content: ''; display: block; height: 4px; width: 60px; background: rgba(255,255,255,0.2); margin: 8px auto 0; border-radius: 2px; }
.mini-line-tech { height: 4px; background: #e3f0ff; border-radius: 2px; margin-bottom: 6px; }

/* TplMinimal 风格缩略图 */
.tpl-mini-minimal { padding: 14px; }
.tpl-mini-minimal .mini-hdr-minimal { height: 14px; width: 70%; background: #111; border-radius: 2px; margin-bottom: 6px; }
.mini-line-divider { height: 1px; background: #e0e0e0; margin: 8px 0; }
.tpl-mini-minimal .mini-body-minimal { flex: 1; }
.mini-body-minimal .mini-line { height: 2px; background: #e0e0e0; border-radius: 2px; margin-bottom: 5px; }
.mini-body-minimal .mini-line.short { width: 60%; }

/* 底部 */
.template-footer {
  padding: 12px 14px;
  display: flex;
  justify-content: space-between;
  align-items: center;
  background: #fff;
}
.template-name { font-size: 14px; font-weight: 600; color: #333; margin-bottom: 4px; }
.template-meta { display: flex; align-items: center; gap: 6px; }
.layout-tag { font-size: 11px; color: #999; }

/* 确认栏 */
.confirm-bar {
  margin-top: 20px;
  padding-top: 16px;
  border-top: 1px solid #eee;
  text-align: center;
}
</style>
