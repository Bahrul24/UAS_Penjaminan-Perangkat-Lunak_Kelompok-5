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

        if (PHP_OS_FAMILY === 'Windows') {

        $options->setBinary(
            'C:\\Users\\muham\\Downloads\\Semester 6\\Penjaminan Kualitas Perangkat Lunak\\chrome-win64\\chrome-win64\\chrome.exe'
            );
        }

        $options->addArguments([
            '--headless=new',
            '--disable-gpu',
            '--no-sandbox',
            '--disable-dev-shm-usage',
            '--window-size=1920,1080'
        ]);

        $capabilities = DesiredCapabilities::chrome();

        $capabilities->setCapability(
            ChromeOptions::CAPABILITY,
            $options
        );

        $this->driver = RemoteWebDriver::create(
            $host,
            $capabilities
        );
    }

    public function testOrderFormFlow()
    {
        // Buka halaman utama
        $this->driver->get($this->baseUrl);

        // Validasi halaman berhasil dibuka
        $bodyText = $this->driver
            ->findElement(WebDriverBy::tagName('body'))
            ->getText();

        $this->assertStringContainsString(
            'CraftApparel',
            $bodyText
        );

        // Pilih jenis pakaian
        $this->driver
            ->findElement(WebDriverBy::id('productType'))
            ->sendKeys('tshirt');

        // Isi jumlah
        $this->driver
            ->findElement(WebDriverBy::id('quantity'))
            ->sendKeys('100');

        // Pilih custom = false
        $this->driver
            ->findElement(WebDriverBy::id('isCustom'))
            ->sendKeys('false');

        // Isi complexity
        $complexity = $this->driver
            ->findElement(WebDriverBy::id('complexity'));

        $complexity->clear();

        $complexity->sendKeys('1');

        // Isi catatan
        $this->driver
            ->findElement(WebDriverBy::id('notes'))
            ->sendKeys('System Testing CraftApparel');

        // Submit form
        $this->driver
            ->findElement(
                WebDriverBy::cssSelector(
                    '[data-testid="btn-submit"]'
                )
            )
            ->click();

        // Tunggu backend memproses
        sleep(2);

        // Ambil seluruh teks halaman hasil
        $resultText = $this->driver
            ->findElement(WebDriverBy::tagName('body'))
            ->getText();

        // Validasi response muncul
        $this->assertNotEmpty($resultText);

        // Validasi status HTTP muncul
        $this->assertStringContainsString(
            'Status HTTP',
            $resultText
        );

        // Validasi response sukses
        $this->assertStringContainsString(
            'success',
            strtolower($resultText)
        );
    }

    protected function tearDown(): void
    {
        if ($this->driver) {
            $this->driver->quit();
        }
    }
}