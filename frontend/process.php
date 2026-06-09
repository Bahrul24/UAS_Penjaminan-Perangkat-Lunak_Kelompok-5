<?php
$type = $_POST['type'];
$qty = $_POST['qty'];
$isCustom = $_POST['isCustom'];
$complexity = $_POST['complexity'];
$notes = $_POST['notes'];

$payload = json_encode([
    "type" => $type,
    "qty" => $qty,
    "isCustom" => $isCustom,
    "complexity" => $complexity,
    "notes" => $notes
]);

$ch = curl_init('http://localhost:8080/api/order');
curl_setopt($ch, CURLOPT_RETURNTRANSFER, true);
curl_setopt($ch, CURLOPT_POST, true);
curl_setopt($ch, CURLOPT_POSTFIELDS, $payload);
curl_setopt($ch, CURLOPT_HTTPHEADER, [
    'Content-Type: application/json',
    'Content-Length: ' . strlen($payload)
]);

$response = curl_exec($ch);
$httpCode = curl_getinfo($ch, CURLINFO_HTTP_CODE);
curl_close($ch);

echo "<h2>Status HTTP: " . $httpCode . "</h2>";
echo "<div data-testid='api-response'>" . $response . "</div>";

echo "<h3>Catatan Anda:</h3>";
echo "<div data-testid='display-notes'>" . $notes . "</div>";
?>