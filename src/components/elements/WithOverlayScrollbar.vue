<script setup lang="ts">
import {computed, onMounted, ref, Ref} from "vue";

const props = withDefaults(defineProps<{
    sizerClass?: string,
    sizerStyle?: string,
    clazz?: string,
    hideTimeout?: number | undefined,
    scrollbarBGColor?: string,
    scrollbarFGColor?: string,
}>(), {
    sizerClass: "",
    sizerStyle: "",
    clazz: "",
    hideTimeout: undefined,
    scrollbarBGColor: "rgba(0, 0, 0, 0.14)",
    scrollbarFGColor: "rgba(0, 0, 0, 0.4)",
})

const scrollbarFGOpaque = computed(() => {
    const temp = document.createElement("div");
    temp.style.color = props.scrollbarFGColor;
    document.body.appendChild(temp);
    const computedColor = getComputedStyle(temp).color;
    document.body.removeChild(temp);

    const match = computedColor.match(/rgba?\((\d+), \s*(\d), \s*(\d+)(?:,\s*([\d.]+))?\)/);  // ai
    const [, r, g, b, _] = match;
    return `rgba(${r}, ${g}, ${b}, 1)`;
})

const useNativeScrollbar = /iPhone|iPad|iPod|Macintosh/.test(navigator.userAgent)  // by AI


const container: Ref<HTMLElement> = ref(null);
const content: Ref<HTMLElement> = ref(null);
const scrollbar: Ref<HTMLElement> = ref(null);
const thumb: Ref<HTMLElement> = ref(null);


class OverlayScrollbar {  // mostly AI and I didn't want to fully unwrap this class
    private container: HTMLElement;
    private content: HTMLElement;
    private scrollbar: HTMLElement;
    private thumb: HTMLElement;
    private isDragging: boolean;
    private hideTimeout: any;
    private dragStartY: any;
    private dragStartTop: number;
    constructor() {
        this.container = container.value;
        this.content = content.value;
        this.scrollbar = scrollbar.value;
        this.thumb = thumb.value;

        this.isDragging = false;
        this.hideTimeout = null;

        this.init();
    }

    init() {
        this.updateThumb();
        this.bindEvents();
    }

    bindEvents() {
        // Content scroll events
        this.container.addEventListener('scroll', () => {
            this.updateThumb();
            this.showScrollbar();
        });

        if (props.hideTimeout != undefined) {
            this.container.addEventListener('mouseenter', () => {
                this.showScrollbar();
            });
            this.container.addEventListener('mouseleave', () => {
                if (!this.isDragging) {
                    this.hideScrollbar();
                }
            });
        } else {
            this.showScrollbar()
        }

        // Thumb drag events
        this.thumb.addEventListener('mousedown', (e) => {
            this.startDrag(e);
        });

        document.addEventListener('mousemove', (e) => {
            if (this.isDragging) {
                this.drag(e);
            }
        });

        document.addEventListener('mouseup', () => {
            this.endDrag();
        });

        // Track click
        this.scrollbar.addEventListener('click', (e) => {
            if (e.target === this.scrollbar) {
                this.jumpToPosition(e);
            }
        });

        // Window resize
        window.addEventListener('resize', () => {
            this.updateThumb();
        });
    }

    updateThumb() {
        requestAnimationFrame(() => {
            const contentHeight = this.content.scrollHeight;
            const containerHeight = this.content.clientHeight;
            const scrollTop = this.container.scrollTop;

            if (contentHeight <= containerHeight) {
                this.scrollbar.style.display = 'none';
                return;
            }

            this.scrollbar.style.display = 'block';

            const thumbHeight = Math.max(
                (containerHeight / contentHeight) * containerHeight - 12,
                20
            );
            const thumbTop = (scrollTop / (contentHeight - containerHeight)) *
                (containerHeight -12 - thumbHeight);

            this.thumb.style.height = thumbHeight + 'px';
            this.thumb.style.top = thumbTop + 'px';
        })
    }

    showScrollbar() {
        this.scrollbar.classList.add('overlay-scrollbar__scrollbar--visible');
        clearTimeout(this.hideTimeout);
    }

    hideScrollbar() {
        this.hideTimeout = setTimeout(() => {
            this.scrollbar.classList.remove('overlay-scrollbar__scrollbar--visible');
        }, props.hideTimeout ?? 1000);
    }

    startDrag(e) {
        this.isDragging = true;
        this.thumb.classList.add('overlay-scrollbar__thumb--dragging');
        this.dragStartY = e.clientY;
        this.dragStartTop = parseInt(this.thumb.style.top) || 0;
        e.preventDefault();
    }

    drag(e) {
        if (!this.isDragging) return;

        const deltaY = e.clientY - this.dragStartY;
        const newTop = this.dragStartTop + deltaY;

        const containerHeight = this.content.clientHeight;
        const thumbHeight = this.thumb.offsetHeight;
        const maxTop = containerHeight * 0.9 - thumbHeight;

        const clampedTop = Math.max(0, Math.min(newTop, maxTop));

        const scrollRatio = clampedTop / (maxTop);
        const contentHeight = this.content.scrollHeight;
        const maxScroll = contentHeight - containerHeight;

        this.container.scrollTop = scrollRatio * maxScroll;
    }

    endDrag() {
        this.isDragging = false;
        this.thumb.classList.remove('overlay-scrollbar__thumb--dragging');
        if (props.hideTimeout != undefined) this.hideScrollbar();
    }

    jumpToPosition(e) {
        const rect = this.scrollbar.getBoundingClientRect();
        const clickY = e.clientY - rect.top;

        const containerHeight = this.content.clientHeight;
        const thumbHeight = this.thumb.offsetHeight;
        const maxTop = containerHeight * 0.9 - thumbHeight;

        const newTop = Math.max(0, Math.min(clickY - thumbHeight / 2, maxTop));
        const scrollRatio = newTop / maxTop;

        const contentHeight = this.content.scrollHeight;
        const maxScroll = contentHeight - containerHeight;

        this.container.scrollTop = scrollRatio * maxScroll;
    }
}


onMounted(() => {
    if (!useNativeScrollbar) new OverlayScrollbar();
})


</script>

<template>
    <div
        class="overlay-scrollbar__container"
        :class="[sizerClass, useNativeScrollbar ? '' : 'overlay-scrollbar__container--hide-native']" :style="sizerStyle"
        ref="container"
    >
        <div
            class="overlay-scrollbar__content"
            :class="clazz"
            ref="content"
        >
            <slot/>
        </div>
        <div
            class="overlay-scrollbar__scrollbar"
            :class="props.hideTimeout == undefined ? 'overlay-scrollbar__scrollbar--visible' : ''"
            ref="scrollbar"
            v-if="!useNativeScrollbar"
        >
            <div
                class="overlay-scrollbar__thumb"
                ref="thumb"
            />
        </div>
    </div>
</template>

<style lang="scss">

.overlay-scrollbar {

    &__container {
        position: relative;
        overflow: hidden;
        overflow-y: scroll;

        &--hide-native {
            scrollbar-width: none; /* Firefox */
            -ms-overflow-style: none; /* IE/Edge */
            &::-webkit-scrollbar {
                display: none; /* Webkit Browser */
            }
        }
    }

    &__content {
        height: 100%;
    }


    &__scrollbar {
        position: fixed;
        top: 6px;
        right: 4px;
        width: 8px;
        height: calc(100% - 12px);
        background: v-bind(scrollbarBGColor);
        border-radius: 4px;
        opacity: 0;
        transition: opacity 0.3s ease;
        z-index: 10;

        &--visible {
            opacity: 1;
        }
    }

    &__thumb {
        position: absolute;
        width: 100%;
        background: v-bind(scrollbarFGColor);
        border-radius: 4px;
        cursor: pointer;
        transition: background 0.2s ease;

        &:hover, &--dragging {
            background: color-mix(in srgb, v-bind(scrollbarFGColor), v-bind(scrollbarFGOpaque) 25%);
        }
    }
}

</style>