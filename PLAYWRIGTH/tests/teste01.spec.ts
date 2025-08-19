import { test, expect } from '@playwright/test';

test('test', async ({ page }) => {
  await page.goto('https://www.google.com/?gws_rd=ssl');
  await page.getByRole('combobox', { name: 'Pesquisar' }).click();
  await page.getByRole('combobox', { name: 'Pesquisar' }).fill('mega sena');
  await page.getByRole('link', { name: 'Mega-Sena - Portal Loterias' }).click();
  await page.getByRole('button', { name: 'Aceitar' }).click();
  await page.getByRole('link', { name: 'Aposte agora' }).click();
});