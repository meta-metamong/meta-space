<template>
    <div ref="animationContainer" class="lottie-container"></div>
  </template>
  
  <script setup>
  import { ref, onMounted, onBeforeUnmount } from 'vue';
  import lottie from 'lottie-web';
  
  const props = defineProps({
    loop: {
      type: Boolean,
      default: true
    },
    autoplay: {
      type: Boolean,
      default: true
    }
  });
  
  const animationContainer = ref(null);
  let animationInstance = null;
  
  onMounted(() => {
    animationInstance = lottie.loadAnimation({
      container: animationContainer.value,
      renderer: 'svg',
      loop: props.loop,
      autoplay: props.autoplay,
      path: new URL('@/resources/json/loading.json', import.meta.url).href
    });
  });
  
  onBeforeUnmount(() => {
    if (animationInstance) {
      animationInstance.destroy();
    }
  });
  </script>
  
  <style scoped>
  .lottie-container {
    width: 150px;
    height: 150px;
    margin: auto;
  }
  </style>
  