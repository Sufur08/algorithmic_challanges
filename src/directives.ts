import {App} from "vue";


function setSelection(el, start, end) {
    const type = el.type
    el.type = 'text'
    el.setSelectionRange(start, end);
    el.type = type
}
function selectAll(el) {
    setSelection(el, 0, el.value.length)
}
function moveCurserEnd(el) {
    setSelection(el, el.value.length, el.value.length)
}

export const vFocus = {
    mounted: (el, binding) => {
        vFocus.update(el, binding)
    },
    update: (el, binding) => {
        if (binding.value == false) return;
        el.focus()
        if (binding.modifiers.select) {
            selectAll(el)
        }
    }
}
export const vFocusEnd = {
    mounted: (el, binding) => {
        const oldHandler = el.onfocus;
        el.onfocus = (e) => {
            if (oldHandler) oldHandler(e);
            setTimeout(() => {
                moveCurserEnd(el)
            }, 0);
        }
    }
}
export function applyDirectives(app: App<Element>) {



    app.directive('focus', vFocus)

    app.directive('focus-end', vFocusEnd)

}
