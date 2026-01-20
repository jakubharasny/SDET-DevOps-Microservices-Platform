import { expect, test } from "@playwright/test";

test("loads countries after click", async ({ page }) => {
  await page.goto("/");

  await page.getByRole("button", { name: "Click this button to show all the countries" }).click();

  await expect(page.getByText("Austria")).toBeVisible();
  await expect(page.getByText("EUR")).toBeVisible();
});
