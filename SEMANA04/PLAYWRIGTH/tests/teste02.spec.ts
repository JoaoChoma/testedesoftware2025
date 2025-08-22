import { test, expect } from '@playwright/test';

test('test', async ({ page }) => {
  await page.goto('https://www.youtube.com/');

  // Campo de pesquisa (nome acessível em inglês)
  const searchBox = page.getByRole('combobox', { name: 'Search' });
  await searchBox.click();
  await searchBox.fill('teste funcional');
  await searchBox.press('Enter');

  // Aguarda por um vídeo nos resultados
  await page.waitForSelector('ytd-video-renderer', { timeout: 10000 });

  // Verifica se o título da página contém o termo
  await expect(page).toHaveTitle(/teste funcional/i);
});