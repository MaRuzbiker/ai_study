<template>
  <component :is="templateComponent" :data="data" :theme="data.theme" />
</template>

<script setup lang="ts">
import { computed } from 'vue'
import TplModern from './TplModern.vue'
import TplClassicNew from './TplClassicNew.vue'
import TplSimple from './TplSimple.vue'
import TplCard from './TplCard.vue'
import TplBusiness from './TplBusiness.vue'

const props = defineProps<{
  templateId: number | null
  data: Record<string, any>
}>()

const templateMap: Record<number, any> = {
  1: TplModern,
  2: TplClassicNew,
  3: TplSimple,
  4: TplCard,
  5: TplBusiness,
}

const templateComponent = computed(() => {
  return templateMap[props.templateId || 1] || TplModern
})
</script>
