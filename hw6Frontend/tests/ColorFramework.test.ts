import { framework } from "../src/lib/ColorFrameworkImpl"

const json = {
  "dataURI": "data:image/jpg;base64,/9j/4AAQSkZJRgA",
  "fileTypes": [".pdf", ".jpg"],
  "numColors": 4,
  "output": [-12345, 123, 0, 91823]
}

beforeEach(() => {
  framework.setJson(json);
});

test("test framework.getColors()", () => {
  expect(framework.getColors()).toStrictEqual(["#FFCFC7", "#00007B", "#000000", "#0166AF"]);
  framework.setJson(null);
  expect(framework.getColors()).toStrictEqual([]);
})

test("test framework.isDark()", () => {
  expect(framework.isDark("#FFFFFF")).toBe(false);
  expect(framework.isDark("#00007B")).toBe(true);
})

test("test framework.getUri()", () => {
  expect(framework.getUri()).toBe("data:image/jpg;base64,/9j/4AAQSkZJRgA");
  framework.setJson(null);
  expect(framework.getUri()).toBe("");
})

test("test framework.getNumColors()", () => {
  expect(framework.getNumColors()).toBe(4);
  framework.setJson(null);
  expect(framework.getNumColors()).toBe(0);
})

test("test framework.getFileTypesString()", () => {
  expect(framework.getFileTypesString()).toBe(".pdf, .jpg");
  framework.setJson(null);
  expect(framework.getFileTypesString()).toBe("");
})

test("test framework.getFileTypes()", () => {
  expect(framework.getFileTypes()).toStrictEqual([".pdf", ".jpg"]);
  framework.setJson(null);
  expect(framework.getFileTypes()).toStrictEqual([]);
})

test("test framework.getJson()", () => {
  expect(framework.getJson()).toStrictEqual(json);
  framework.setJson(null);
  expect(framework.getJson()).toStrictEqual(null);
})