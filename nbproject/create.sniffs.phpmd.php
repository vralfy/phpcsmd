<?php
#define('LOCATION', '/usr/share/php/data/PHP_PMD/resources/rulesets');
define('LOCATION', '/data/opt/pear/data/PHP_PMD/resources/rulesets');
function xml2array($data)
{
    $data = file_get_contents($data);

    $data = explode("\n", $data);
    foreach ($data as &$line) {
        $line = trim($line);
    }
    $data = implode(' ', $data);

    $data = preg_replace('/<\!\[CDATA\[|\]\]>/', '', $data);
    $data = preg_replace('/<property[^>]*?\/>/', '', $data);
    $data = preg_replace('/<properties \/>/', '', $data);
    $data = preg_replace('/<example \/>/', '', $data);

    $data = preg_replace('/<example.*<\/example>/iU', '', $data);
    $data = preg_replace('/<properties.*<\/properties>/iU', '', $data);
    $data = preg_replace('/<property.*<\/property>/iU', '', $data);



    return json_decode(json_encode((array) simplexml_load_string($data)));
}

$classStr = 'package de.foopara.phpcsmd.option.phpmd;

public class DynamicPhpmdSniffRegistry extends GenericPhpmdSniffRegistry {
    public DynamicPhpmdSniffRegistry() {
';

$attr = '@attributes';
$dir = opendir(LOCATION);
while ($sniff = readdir($dir)) {
    if (!in_array($sniff, array('.', '..'))) {
        echo $sniff . "\n";
        echo "++++++++++++++++++++++++++++++++++++++++++++++++++\n";
        $classStr .= '        this.add("' . preg_replace('/\.xml$/i', '', $sniff) . '");' . "\n";
        $rules = xml2array(LOCATION . '/' . $sniff);
        $rules = $rules->rule;
        foreach ($rules as $rule) {
            echo $rule->$attr->class . "\n";
            echo trim($rule->description) . "\n";
            echo "-----------\n";

            $type = 'null';
            if ($sniff == 'codesize.xml' || $sniff == 'complexity.xml') {
                $type = '"complex"';
            }

            $classStr .= '        this.add(new PhpmdSniff("' . $rule->$attr->name . '", "' . trim($rule->description) . '", ' . $type . '));' . "\n";
        }
    }
}

$classStr .= '}}';

file_put_contents('../src/de/foopara/phpcsmd/option/phpmd/DynamicPhpmdSniffRegistry.java', $classStr);
