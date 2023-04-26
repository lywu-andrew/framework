<script lang="ts">
  import RangeSlider from "svelte-range-slider-pips";
  import { Circle3 } from "svelte-loading-spinners";
  import { framework } from "./ColorFrameworkImpl";

  /**
   * Current value of the slider (determines the number of colors the framework generates). 
   */
  let value = [framework.getNumColors()];

  /**
   * Colors in the framework's output.
   */
  let colors: string[] = framework.getColors();

  /**
   * Controls whether to show loading spinner.
   */
  let loading: boolean = false;

  /**
   * Sets number of colors in the output
   */
  async function onSliderChange(e: Event) {
    const x = e["detail"]["value"];
    value = [x];
    loading = true;
    const response = await fetch(`http://localhost:8080/sliderChange?x=${x}`);
    let json = await response.json();
    framework.setJson(json);
    colors = framework.getColors();
    loading = false;
  }
</script>

<div class="row">
  <div class="column">
    <img src={framework.getUri()} alt="uploaded_image" class="ml-10 mt-10 mr-10 float-left" height={window.innerHeight / 2} />
  </div>
  <div class="column">
    <p class="slider-text mb-2 text-lg text-left text-gray-500">
      Move slider to change the number of colors in the theme.
    </p>
    <p class="slider-text-sm mb-2 text-sm text-left text-gray-500">
      (If your image has too few colors, there will be repeats in the generated theme.)
    </p>
    <div class="slider">
      <RangeSlider
        values={value}
        pips={true}
        min={5}
        max={20}
        float={true}
        all="label"
        on:change={(e) => {
          onSliderChange(e);
        }}
      />
    </div>
    {#if loading}
      <div class="flex flex-col justify-center mx-3">
        <div class="flex justify-center">
          <Circle3 />
        </div>
        <p class="mb-2 text-center text-gray-500 text-md">Loading...</p>
      </div>
    {:else}
      <div class="grid grid-cols-4 gap-7 mx-28 mb-20">
        {#each colors as color}
          <div class="p-5 rounded-lg" style="background-color: {color}">
            {#if framework.isDark(color)}
              <p class="text-center text-white text-sm">{color}</p>
            {:else}
              <p class="text-center text-black text-sm">{color}</p>
            {/if}
          </div>
        {/each}
      </div>
    {/if}
  </div>
</div>
