<?php

use PHPUnit\Framework\TestCase;

class FrontendCoverageTest extends TestCase
{
    public function testIndexPageLoads()
    {
        ob_start();
        include __DIR__ . '/../frontend/index.php';
        $output = ob_get_clean();

        $this->assertStringContainsString(
            'Formulir Pemesanan CraftApparel',
            $output
        );

        $this->assertStringContainsString(
            'orderForm',
            $output
        );
    }

    public function testProcessPageExecution()
    {
        $_POST['type'] = 'tshirt';
        $_POST['qty'] = 100;
        $_POST['isCustom'] = 'false';
        $_POST['complexity'] = 1;
        $_POST['notes'] = 'coverage test';

        ob_start();

        include __DIR__ . '/../frontend/process.php';

        $output = ob_get_clean();

        $this->assertStringContainsString(
            'Catatan Anda',
            $output
        );

        $this->assertStringContainsString(
            'coverage test',
            $output
        );
    }
}