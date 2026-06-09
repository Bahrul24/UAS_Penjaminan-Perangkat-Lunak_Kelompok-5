<?php

use PHPUnit\Framework\TestCase;
use Facebook\WebDriver\Remote\RemoteWebDriver;
use Facebook\WebDriver\Remote\DesiredCapabilities;
use Facebook\WebDriver\Chrome\ChromeOptions;
use Facebook\WebDriver\WebDriverBy;

require __DIR__ . '/../vendor/autoload.php';

class SystemTest extends TestCase
{
    private $driver;
    private $baseUrl = 'http://localhost:8000';

    protected function setUp(): void
    {
        $host = 'http://localhost:9515';

        $options = new ChromeOptions();

        // path chrome kamu
        $options->setBinary(
            'C:\Users\muham\Downloads\Semester 6\Penjaminan Kualitas Perangkat Lunak\chrome-win64\chrome-win64\chrome.exe'
        );

        // mode headless biar stabil
        $options->addArguments([
            '--headless',
            '--disable-gpu',
            '--no-sandbox'
        ]);

        $capabilities = DesiredCapabilities::chrome();
        $capabilities->setCapability(ChromeOptions::CAPABILITY, $options);

        $this->driver = RemoteWebDriver::create($host, $capabilities);
    }

    public function testOrderFormFlow()
    {
        // 1. buka halaman
        $this->driver->get($this->baseUrl);

        // 2. pastikan halaman terbuka
        $this->assertStringContainsString(
            'CraftApparel',
            $this->driver->getTitle() ?: $this->driver->findElement(WebDriverBy::tagName('body'))->getText()
        );

        // 3. isi form
        $this->driver->findElement(WebDriverBy::id('productType'))->sendKeys('tshirt');

        $this->driver->findElement(WebDriverBy::id('quantity'))->sendKeys('100');

        $this->driver->findElement(WebDriverBy::id('isCustom'))->sendKeys('false');

        $this->driver->findElement(WebDriverBy::id('complexity'))->clear();
        $this->driver->findElement(WebDriverBy::id('complexity'))->sendKeys('1');

        $this->driver->findElement(WebDriverBy::id('notes'))->sendKeys('unit test order');

        // 4. submit
        $this->driver
            ->findElement(WebDriverBy::cssSelector('[data-testid="btn-submit"]'))
            ->click();

        // 5. tunggu proses backend + JS
        sleep(1);

        // 6. ambil semua isi halaman
        $bodyText = $this->driver
            ->findElement(WebDriverBy::tagName('body'))
            ->getText();

        // 7. validasi hasil sukses
        $this->assertNotEmpty($bodyText);
        $this->assertStringContainsString('success', $bodyText);
    }

    protected function tearDown(): void
    {
        if ($this->driver) {
            $this->driver->quit();
        }
    }
}