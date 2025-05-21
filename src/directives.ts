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

export const vAnimatedIf = {
    beforeMount: (el, binding) => {
    },
    mounted: (el, binding) => {
        vAnimatedIf.updated(el, binding)

        requestAnimationFrame(() => {
            if (!binding.value) el.style.display = 'none';
        })
    },
    updated: (el, binding) => {
        const transitionTime = binding.modifiers.duration ?? .4
        const transitionString = (before: string) => `all ${transitionTime}s ease${before ? ", " + before : ''}`
        const before = { ...el.style, display: "" }
        function setTo0() {
            if (binding.modifiers.height)
            {
                el.style.height = "0";
                el.style.paddingBlock = "0";
                el.style.marginBlock = "0";
            }
            if (binding.modifiers.width)
            {
                el.style.width = "0";
                el.style.paddingInline = "0";
                el.style.marginInline = "0";
            }
        }
        function setToExtended() {
            if (binding.modifiers.height) {
                el.style.height = el.scrollHeight + 'px';
                el.style.paddingBlock = before.paddingBlock;
                el.style.marginBlock = before.marginBlock;
            }
            if (binding.modifiers.width) {
                el.style.width = el.scrollWidth + 'px';
                el.style.paddingInline = before.paddingInline;
                el.style.marginInline = before.marginInline;
            }
        }
        if (binding.value) {
            el.style.display = ""
            el.style.overflow = "hidden";
            setTo0();
            el.style.transition = transitionString(before.transition);
            requestAnimationFrame(() => {
                setToExtended();
                setTimeout(() => {
                    el.style = before;
                }, (transitionTime + .01) * 1000)
            })
        } else {
            el.style.overflow = "hidden";
            setToExtended();
            el.style.transition = transitionString(before.transition);
            requestAnimationFrame(() => {
                setTo0();
                setTimeout(() => {
                    el.style = before;
                    el.style.display = "none"
                    console.log("display none")
                }, (transitionTime + .01) * 1000)
            })
        }
    }
}

export function applyDirectives(app: App<Element>) {



    app.directive('focus', vFocus)

    app.directive('focus-end', vFocusEnd)

    app.directive('animated-if', vAnimatedIf)


}
