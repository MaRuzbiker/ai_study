<template>
  <div class="resume-edit-layout">
    <div class="edit-toolbar">
      <div class="toolbar-left">
        <el-button text @click="router.push('/resume')"><el-icon><ArrowLeft /></el-icon>返回</el-button>
        <el-divider direction="vertical" />
        <el-input v-model="form.title" placeholder="简历标题" size="small" style="width:220px" maxlength="60" />
      </div>
      <div class="toolbar-right">
        <el-select v-model="currentTemplateId" size="default" style="width:160px">
          <el-option v-for="t in templateOptions" :key="t.id" :label="t.label" :value="t.id" />
        </el-select>
        <el-button @click="saveAsDraft" :loading="saving">保存草稿</el-button>
        <el-button type="primary" @click="saveAndPublish" :loading="saving">保存发布</el-button>
        <el-button type="success" @click="handleDownloadPdf" :loading="pdfLoading"><el-icon><Download /></el-icon>下载PDF</el-button>
      </div>
    </div>
    <div class="edit-main">
      <div class="edit-form-panel">
        <el-scrollbar height="100%">
          <div class="form-inner">
            <div class="section-block fixed-section">
              <div class="block-header"><span class="block-icon">&#128100;</span><span class="block-title">个人信息</span></div>
              <el-form label-width="72px" size="default">
                <el-row :gutter="10">
                  <el-col :span="12"><el-form-item label="姓名"><el-input v-model="form.personal.name" placeholder="张三" /></el-form-item></el-col>
                  <el-col :span="12"><el-form-item label="性别"><el-select v-model="form.personal.gender" style="width:100%"><el-option label="男" value="男" /><el-option label="女" value="女" /></el-select></el-form-item></el-col>
                  <el-col :span="12"><el-form-item label="电话"><el-input v-model="form.personal.phone" placeholder="138xxxx8888" /></el-form-item></el-col>
                  <el-col :span="12"><el-form-item label="邮箱"><el-input v-model="form.personal.email" placeholder="example@qq.com" /></el-form-item></el-col>
                  <el-col :span="24"><el-form-item label="求职意向"><el-input v-model="form.personal.targetPosition" placeholder="例如：Java 后端开发工程师" /></el-form-item></el-col>
                </el-row>
              </el-form>
            </div>
            <div class="draggable-hint"><el-icon><Rank /></el-icon> 拖动模块可调整简历中的板块顺序</div>
            <draggable v-model="sectionOrder" item-key="key" handle=".drag-handle" :animation="200" ghost-class="drag-ghost">
              <template #item="{ element }">
                <div v-if="element === 'summary'" class="section-block draggable-block">
                  <div class="block-header"><el-icon class="drag-handle" style="cursor:grab;color:#bbb"><Rank /></el-icon><span class="block-icon">&#128161;</span><span class="block-title">自我评价</span></div>
                  <el-input v-model="form.summary" type="textarea" :rows="4" placeholder="简要描述你的优势、职业目标和性格特点" maxlength="500" show-word-limit />
                </div>
                <div v-if="element === 'education'" class="section-block draggable-block">
                  <div class="block-header"><el-icon class="drag-handle" style="cursor:grab;color:#bbb"><Rank /></el-icon><span class="block-icon">&#127891;</span><span class="block-title">教育背景</span></div>
                  <div v-for="(edu, idx) in form.education" :key="idx" class="sub-block">
                    <el-row :gutter="10">
                      <el-col :span="12"><el-form-item label="学校" size="small"><el-input v-model="edu.school" placeholder="学校名称" /></el-form-item></el-col>
                      <el-col :span="12"><el-form-item label="学历" size="small"><el-select v-model="edu.degree" style="width:100%"><el-option label="高中" value="高中" /><el-option label="大专" value="大专" /><el-option label="本科" value="本科" /><el-option label="硕士" value="硕士" /><el-option label="博士" value="博士" /></el-select></el-form-item></el-col>
                      <el-col :span="12"><el-form-item label="专业" size="small"><el-input v-model="edu.major" placeholder="专业名称" /></el-form-item></el-col>
                      <el-col :span="12"><el-form-item label="GPA" size="small"><el-input v-model="edu.gpa" placeholder="例如：3.8/4.0" /></el-form-item></el-col>
                      <el-col :span="12"><el-form-item label="开始" size="small"><el-date-picker v-model="edu.startDate" type="month" value-format="YYYY-MM" style="width:100%" /></el-form-item></el-col>
                      <el-col :span="12"><el-form-item label="结束" size="small"><el-date-picker v-model="edu.endDate" type="month" value-format="YYYY-MM" placeholder="留空为至今" style="width:100%" /></el-form-item></el-col>
                    </el-row>
                    <el-button type="danger" plain size="small" @click="form.education.splice(idx, 1)">删除</el-button>
                  </div>
                  <el-button type="default" plain size="small" @click="form.education.push({ school:'', degree:'', major:'', gpa:'', startDate:'', endDate:'' })">+ 添加教育背景</el-button>
                </div>
                <div v-if="element === 'campusExperiences'" class="section-block draggable-block">
                  <div class="block-header"><el-icon class="drag-handle" style="cursor:grab;color:#bbb"><Rank /></el-icon><span class="block-icon">&#127917;</span><span class="block-title">校园经历</span></div>
                  <div v-for="(c, idx) in form.campusExperiences" :key="idx" class="sub-block">
                    <el-row :gutter="10">
                      <el-col :span="12"><el-form-item label="组织/社团" size="small"><el-input v-model="c.organization" placeholder="例如：学生会，科技协会" /></el-form-item></el-col>
                      <el-col :span="12"><el-form-item label="担任角色" size="small"><el-input v-model="c.role" placeholder="例如：主席、部长" /></el-form-item></el-col>
                      <el-col :span="12"><el-form-item label="开始" size="small"><el-date-picker v-model="c.startDate" type="month" value-format="YYYY-MM" style="width:100%" /></el-form-item></el-col>
                      <el-col :span="12"><el-form-item label="结束" size="small"><el-date-picker v-model="c.endDate" type="month" value-format="YYYY-MM" placeholder="留空为至今" style="width:100%" /></el-form-item></el-col>
                      <el-col :span="24"><el-form-item label="经历描述" size="small"><el-input v-model="c.description" type="textarea" :rows="3" placeholder="描述你负责的工作和取得的成果" /></el-form-item></el-col>
                    </el-row>
                    <el-button type="danger" plain size="small" @click="form.campusExperiences.splice(idx, 1)">删除</el-button>
                  </div>
                  <el-button type="default" plain size="small" @click="form.campusExperiences.push({ organization:'', role:'', startDate:'', endDate:'', description:'' })">+ 添加校园经历</el-button>
                </div>
