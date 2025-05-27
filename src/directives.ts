import {App} from "vue";

function measureCss(el: HTMLElement, css: string) {
    if (!css) return
    const temp = document.createElement("div");
    temp.style.position = "absolute";
    temp.style.visibility = "hidden";
    temp.style.width = css;
    const parent = el.parentElement;
    parent.appendChild(temp);
    const px = temp.offsetWidth;
    parent.removeChild(temp);
    return px;
}

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
        el.dataset.vAnimatingIf = "0";
    },
    mounted: (el, binding) => {
     //   binding.modifiers = { ...binding.modifiers, transitionTime: 0 };
       // vAnimatedIf.updated(el, binding)    /// create style tag with individual id to el to store keyframes for an animation
        el.style.display = "none"


    },
    updated: (el, binding) => {
        if (binding.value == binding.oldValue) return;
        const timeFactor = binding.modifiers.transitionTime ?? ([undefined, null, ""].includes(el.dataset.vRelativeTransition) ? .0013: Number(el.dataset.vRelativeTransition))
        const xTransitionTime = () => Math.max(0, binding.modifiers.transitionTime ?? (timeFactor * (el.scrollWidth - (measureCss(el, el.dataset.vBaseWidth) ?? 0))))
        const yTransitionTime = () => Math.max(0, binding.modifiers.transitionTime ?? (timeFactor * (el.scrollHeight - (measureCss(el, el.dataset.vBaseHeight) ?? 0))))
        const before = { ...el.style, display: "" }
        const level = Number(el.dataset.vAnimatingIf) + 1
        el.dataset.vAnimatingIf = level
        const transitionString = (old: string = before.transition) => {
            const type = "cubic-bezier(0.25, 0, 0.75, 1)"
            const x = xTransitionTime()
            const y = yTransitionTime()
            return `width ${x}s ${type}, padding-inline ${x}s ${type}, margin-inline ${x}s ${type}, height ${y}s ${type}, padding-block ${y}s ${type}, margin-block ${y}s ${type}${old ? ", " + old : ''}`;
        }
        function setTo0(height = binding.modifiers.height, width = binding.modifiers.width) {
            if (height) {
                el.style.height = el.dataset.vBaseHeight ?? "0";
                el.style.paddingBlock = "0";
                el.style.marginBlock = "0";
            }
            if (width) {
                el.style.width = el.dataset.vBaseWidth ?? "0";
                el.style.paddingInline = "0";
                el.style.marginInline = "0";
            }
        }
        function setToExtended(height = binding.modifiers.height, width = binding.modifiers.width) {
            if (height) {
                el.style.height = el.scrollHeight + 'px';
                el.style.paddingBlock = before.paddingBlock;
                el.style.marginBlock = before.marginBlock;
            }
            if (width) {
                el.style.width = el.scrollWidth + 'px';
                el.style.paddingInline = before.paddingInline;
                el.style.marginInline = before.marginInline;
            }
        }
        console.log(binding)
        function doPrimary(extend: boolean, after: () => any) {
            requestAnimationFrame(() => {
                extend ? setToExtended() : setTo0();
                setTimeout(() => {
                    if (el.dataset.vAnimatingIf != level) return;
                    after();
                }, (binding.modifiers.width ? xTransitionTime() : yTransitionTime()) * 1000);
            })
        }
        function doSecondary(extend: boolean, after: () => any) {
            if ((binding.modifiers.thenHeight && (el.scrollHeight == (el.dataset.vBaseHeight ? measureCss(el, el.dataset.vBaseHeight) : "0px"))) ||
                (binding.modifiers.thenWidth && (el.scrollWidth == (el.dataset.vBaseWidth ? measureCss(el, el.dataset.vBaseWidth) : "0px")))) {
                after(); return;
            }  // padding and margin should be checked as well but idc
            requestAnimationFrame(() => {
                extend ? setToExtended(binding.modifiers.thenHeight ?? false, binding.modifiers.thenWidth ?? false)
                    : setTo0(binding.modifiers.thenHeight ?? false, binding.modifiers.thenWidth ?? false);
                setTimeout(() => {
                    if (el.dataset.vAnimatingIf != level) return;
                    after();
                }, (binding.modifiers.thenWidth ? xTransitionTime() : yTransitionTime()) * 1000);
            })
        }
        function reset() {
            if (el.dataset.vStillAnimating) return;
            el.style = before;
            el.dataset.vAnimatingIf = "0"
//            delete el.dataset.vAnimatingIf;
        }
        el.style.overflow = "hidden";
        if (binding.value) {
            setTo0(binding.modifiers.height || binding.modifiers.thenHeight,
                binding.modifiers.width || binding.modifiers.thenWidth);
            el.style.display = "";
            el.style.transition = transitionString();
            if (binding.modifiers.thenHeight || binding.modifiers.thenWidth) {
                doSecondary(true, () => {
                    doPrimary(true, reset);
                });
            } else {
                doPrimary(true, reset);
            }
        } else {
            setToExtended(binding.modifiers.height || binding.modifiers.thenHeight,
                binding.modifiers.width || binding.modifiers.thenWidth);
            el.style.transition = transitionString();
            doPrimary( false, () => {
                if (binding.modifiers.thenHeight || binding.modifiers.thenWidth) {
                    doSecondary(false, () => {
                        reset()
                        el.style.display = "none";
                    })
                } else {
                    reset()
                    el.style.display = "none";
                }
            })
        }
    }
}

export function applyDirectives(app: App<Element>) {



    app.directive('focus', vFocus)

    app.directive('focus-end', vFocusEnd)

    app.directive('animated-if', vAnimatedIf)


}
