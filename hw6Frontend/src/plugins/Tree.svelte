<script lang="ts">
  import RangeSlider from "svelte-range-slider-pips";
  import { onMount } from "svelte";
  import { framework } from "../lib/ColorFrameworkImpl";

  /**
   * Current value of the slider (determines the angle of tree branches). 
   */
  let angleValue = [15]; 

  /**
   * Colors in the framework's output.
   */
  let colors: string[] = null;

  /**
   * HTML canvas for drawing tree.
   */
  let canvas : HTMLCanvasElement = null;

  /**
   * Context of the HTML canvas.
   */
  let ctx = null;

  onMount(() => {
    // get canvas context
    colors = framework.getColors(); // List of colors
    ctx = canvas.getContext("2d")

    // draw
    drawTree();
    window.addEventListener('resize', onResize);
    return () => window.removeEventListener('resize', onResize);
   });

   /**
    * Redraws tree when window is resized
   */
  function onResize() : void {
    ctx.clearRect(0, 0, ctx.canvas.width, ctx.canvas.height);
    drawTree();
  }

  /**
   * Draws a tree based on the window size.
   */
  function drawTree() : void {
    draw(window.innerWidth / 2, 2 * window.innerHeight / 3, window.innerHeight / 8, 0, 0);
  }

  /**
   * Recursive function to draw the fractal (inspired by a 15-462 S22 mini hw).
   * @param startX x position to start with
   * @param startY y position to start with
   * @param len Length of 1 branch
   * @param angle Angle between branch and y-axis
   * @param level Depth of recursion (will determine color of branch)
   */
  function draw(startX: number, startY: number, len: number, angle: number, level: number) : void {
    ctx.beginPath();
    ctx.save();

    ctx.translate(startX, startY);
    ctx.rotate(angle * Math.PI/180);
    ctx.moveTo(0, 0);
    ctx.lineTo(0, -len);
    ctx.strokeStyle = colors[level % (colors.length)];
    ctx.lineWidth = 4;
    ctx.stroke();

    if ((len < 8) || (colors.length > 10 && level >= colors.length)) { // Base case
      ctx.restore();
      return;
    }
    // Recursive
    draw(0, -len, len * 0.8, -angleValue[0], level + 1);
    draw(0, -len, len * 0.8, +angleValue[0], level + 1);

    ctx.restore();
  }

  /**
   * Sets number of colors in the output
   */
  async function onSliderChange(e: Event) {
    const x = e["detail"]["value"];
    angleValue = [x];

    ctx.clearRect(0, 0, ctx.canvas.width, ctx.canvas.height);
    drawTree();
  }
  
</script>

<canvas bind:this={canvas}
  id='test-canvas'
  width={window.innerWidth}
  height={5 * window.innerHeight / 7}
  />
  <p class="slider-text mb-2 text-lg text-left text-gray-500">
    Move slider to change the angle of the tree branches.
  </p>
<div class="slider">
  <RangeSlider
    values={angleValue}
    pips={true}
    min={10}
    max={40}
    float={true}
    first="label"
    last="label"
    on:change={(e) => {
      onSliderChange(e);
    }}
  />
</div>