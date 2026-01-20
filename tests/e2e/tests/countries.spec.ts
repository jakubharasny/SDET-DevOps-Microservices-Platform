import { expect, test } from "@playwright/test";

test("loads countries after click", async ({ page }) => {
  await page.goto("/");

  await page.getByRole("button", { name: "Click this button to show all the countries" }).click();

  const austriaRow = page.getByRole("row", { name: /Austria/ });

  await expect(austriaRow.getByRole("cell", { name: "Austria", exact: true })).toBeVisible();
  await expect(austriaRow.getByRole("cell", { name: "EUR", exact: true })).toBeVisible();
});
