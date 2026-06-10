import { ref, readonly } from 'vue'

export interface ContextMenuItem {
  label: string
  icon?: string
  action: () => void
  danger?: boolean
}

const visible = ref(false)
const x = ref(0)
const y = ref(0)
const items = ref<ContextMenuItem[]>([])

export function useContextMenu() {
  function show(e: MouseEvent, menuItems: ContextMenuItem[]) {
    e.preventDefault()
    e.stopPropagation()
    visible.value = false
    items.value = menuItems
    x.value = e.clientX
    y.value = e.clientY
    // nextTick 确保 visible=false → true 触发过渡
    requestAnimationFrame(() => {
      visible.value = true
    })
  }

  function hide() {
    visible.value = false
  }

  return {
    visible: readonly(visible),
    x: readonly(x),
    y: readonly(y),
    items: readonly(items),
    show,
    hide
  }
}
