<script lang="ts">
  import "../app.css";
  import { Circle3 } from "svelte-loading-spinners";
  import { TextBox, TextBoxButton } from "fluent-svelte";
  import { framework } from "./ColorFrameworkImpl";

  // styling variables
  let drag_border: boolean;
  let drag_border_color: string =
    "flex flex-col justify-center mx-auto items-center mt-10 w-1/2 h-64 rounded-lg border-2 border-gray-300 cursor-pointer bg-gray-100 border-solid";
  let leave_border_color: string =
    "flex flex-col justify-center mx-auto items-center mt-10 w-1/2 h-64 bg-gray-50 rounded-lg border-2 border-gray-300 cursor-pointer hover:bg-gray-100 border-dashed";

  /**
   * Error in image upload.
   */
  let image_error: boolean = false;

  /**
   * Successful image upload.
   */
  let image_success: boolean = false;

  /**
   * Controls whether to show loading spinner.
   */
  let loading: boolean = false;

  /**
   * Colors in the framework's output.
   */
  let colors: String[] = [];

  /**
   * Input image URL, if any.
   */
  let image_url: string = "";

  /**
   * Error in URL upload.
   */
  let url_error: boolean = false;

  /**
   * Successful URL upload.
   */
  let url_success: boolean = false;

  /**
   * The framework's supported file types.
   */
  let files: string = framework.getFileTypesString();

  /**
   * Checks if a file has been uploaded.
   * @returns Whether file has been uploaded
   */
  function noFileLoaded() : boolean {
    return (framework.getUri() === "");
  }

  /**
   * Uploads a file to the backend.
   */
  async function uploadFile(event: DragEvent) {
    image_error = false;
    image_success = false;
    drag_border = false;
    const end_point: string = "http://localhost:8080/upload";
    const form_data = new FormData();

    let image_file: File = event.dataTransfer.files[0];

    form_data.append("image_file", image_file);
    loading = true;

    try {
      const response = await fetch(end_point, {
        method: "post",
        body: form_data,
      });
      let json = await response.json();
      loading = false;
      if (json["output"].length === 0) {
        image_error = true;
        return;
      }
      framework.setJson(json);
      colors = framework.getColors();
      if (json) {
        files = json["fileTypes"].join(", ");
      } else {
        files = "Loading...";
      }
      image_success = true;
    } catch (error) {
      image_error = true;
      loading = false;
    }
  }

  /**
   * Automatically fetches ColorFrameworkState from backend on load
   */ 
  async function onLoad() {
    image_error = false;
    image_url = '';
    url_error = false;
    loading = false;
    const response = await fetch("http://localhost:8080/init");
    let json = await response.json();
    framework.setJson(json);
    files = framework.getFileTypesString();
  }

  /**
   * Validates the URL string the frontend receives.
   * @param url URL from text box
   * @return Whether URL is valid
   */
  function verifyUrlFileType(url: string) : boolean {
    var filename = url.substring(url.lastIndexOf("/") + 1);
    if (filename === url) {
      return false;
    }
    var fileExtension = filename.substring(filename.lastIndexOf("."));
    if (filename === fileExtension) {
      return false;
    }
    for (var extension of framework.getFileTypes()) {
      if (extension === fileExtension) {
        return true;
      }
    }
    return false;
  }

  /**
   * Attempts to fetch ColorFrameworkState from backend on textbox click
   */
  async function onImageUrlClick() {
    url_error = false;
    url_success = false;
    image_error = false;
    try {
      const validUrl = verifyUrlFileType(image_url);
      if (!validUrl) {
        url_error = true;
        url_success = false;
        return;
      }
    } catch (error) {
      url_error = true;
      url_success = false;
      return;
    }

    loading = true;
    const response = await fetch(`http://localhost:8080/getUrl?x=${image_url}`);
    let json = await response.json();
    loading = false;
    if (json["dataURI"] === "") {
      url_error = true;
      url_success = false;
    } else {
      framework.setJson(json);
      colors = framework.getColors();
      url_success = true;
    }
  }

  /**
   * Clears error fields.
   */
  function onImageUrlClear() : void { 
    url_error = false;
    image_error = false;
  }
</script>

<svelte:window on:load={() => onLoad()} />

{#if loading}
  <div class="loading-spinner">
    <Circle3 size="60" unit="px" />
    <p class="mb-2 text-center text-gray-500 text-md">Loading...</p>
  </div>
{:else}
<div class="title-card">
  {#if noFileLoaded()}
  <p class="mb-2 text-lg text-center text-gray-600">
    To start, give us a supported file.
  </p>
  {:else}
  <p class="mb-2 text-lg text-center text-gray-600">
    You have successfully uploaded a file. Please explore the options on the upper right corner.
  </p>
  {/if}
</div>
  <div class="mx-auto input-textbox">
    <TextBox bind:value={image_url}
    placeholder="Input a URL for supported file types (e.g. http://hello.com/world.png)"
    on:clear={() => onImageUrlClear()}
    >
      <TextBoxButton slot="buttons" on:click={() => onImageUrlClick()}>
        <!-- https://github.com/microsoft/fluentui-system-icons -->
        <svg
          width="16"
          height="16"
          viewBox="0 0 16 16"
          xmlns="http://www.w3.org/2000/svg"
          fill="currentColor"
          class="bi bi-upload"
        >
          <path
            d="M.5 9.9a.5.5 0 0 1 .5.5v2.5a1 1 0 0 0 1 1h12a1 1 0 0 0 1-1v-2.5a.5.5 0 0 1 1 0v2.5a2 2 0 0 1-2 2H2a2 2 0 0 1-2-2v-2.5a.5.5 0 0 1 .5-.5z"
          />
          <path
            d="M7.646 1.146a.5.5 0 0 1 .708 0l3 3a.5.5 0 0 1-.708.708L8.5 2.707V11.5a.5.5 0 0 1-1 0V2.707L5.354 4.854a.5.5 0 1 1-.708-.708l3-3z"
          />
        </svg>
      </TextBoxButton>
    </TextBox>
  </div>
  <div class="flex mx-auto url-error">
    {#if url_error}
      <p class="mb-2 text-sm text-left text-red-500">
        Error: Invalid URL or file type. Please try another URL!
      </p>
    {:else if url_success}
      <p class="mb-2 text-sm text-left text-green-500">
        Success! To view result, select one of the tabs on the upper right corner.
      </p>
    {:else}
      <p class="mb-2 text-sm text-left text-gray-500">
        URL should end in one of the supported file extensions: {files}
      </p>
    {/if}
  </div>
  <!-- File upload box -->
  <div
    on:dragenter={() => (drag_border = true)}
    on:dragleave={() => (drag_border = false)}
    on:dragover|preventDefault
    on:drop|preventDefault={uploadFile}
    class={drag_border ? drag_border_color : leave_border_color}
  >
    <svg
      aria-hidden="true"
      class="mb-3 w-10 h-10 text-gray-400"
      fill="none"
      stroke="currentColor"
      viewBox="0 0 24 24"
      ><path
        stroke-linecap="round"
        stroke-linejoin="round"
        stroke-width="2"
        d="M7 16a4 4 0 01-.88-7.903A5 5 0 1115.9 6L16 6a5 5 0 011 9.9M15 13l-3-3m0 0l-3 3m3-3v12"
      /></svg
    >
    <div>
      <p class="mb-2 text-lg text-center text-gray-500">
        <span class="font-semibold">Drag and drop</span> to upload
      </p>
      <p class="mb-2 text-sm text-center text-gray-400">
        Supported file extension(s): {files}
      </p>
    </div>
    <input name="file" type="file" class="sr-only" />
  </div>
  <div class="flex mx-auto image-error">
    {#if image_error}
      <p class="mb-2 text-sm text-left text-red-500">
        Error: Invalid file type or failure to upload image. Please try another file!
      </p>
    {:else if image_success}
      <p class="mb-2 text-sm text-left text-green-500">
        Success! To view result, select one of the tabs on the upper right corner.
      </p>
    {/if}
  </div>
{/if}
