<?php

use PHPUnit\Framework\TestCase;

class ApiProxyTest extends TestCase {

    public function testSendOrderToJavaAPI() {

        $payload = [
            "type" => "tshirt",
            "qty" => 100,
            "isCustom" => false,
            "complexity" => 0,
            "notes" => "unit test"
        ];

        $options = [
            'http' => [
                'header'  => "Content-Type: application/json",
                'method'  => 'POST',
                'content' => json_encode($payload)
            ]
        ];

        $context = stream_context_create($options);

        $response = file_get_contents("http://localhost:8080/api/order", false, $context);

        $this->assertNotNull($response);
        $this->assertStringContainsString("success", $response);
    }
}