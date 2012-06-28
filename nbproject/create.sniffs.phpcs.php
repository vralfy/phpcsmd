<?php
//define('LOCATION', '/usr/share/php/PHP/CodeSniffer/Standards');
define('LOCATION', '/data/opt/pear/share/pear/PHP/CodeSniffer/Standards');

$STANDARDS = array(
    'Generic', 'PEAR', 'Squiz', 'Zend', 'MySource'
);

function readSniffs($standard, $sniffclass, &$res) {
    $dir = opendir(LOCATION . '/' . $standard . '/Sniffs/' . $sniffclass);
    while ($sniff = readdir($dir)) {
        if (!in_array($sniff, array('.', '..'))) {
            $sniffName = $standard . '.' . $sniffclass . '.' . preg_replace('/Sniff\.php$/', '', $sniff);
            echo '-- -- -- ' . $sniffName . ' (' . $sniff . ')' . "\n";
            $res[$standard][$sniffclass][] = $sniffName;
        }
    }
    asort($res[$standard][$sniffclass]);
    ksort($res[$standard]);
}


$sniffs = array();
foreach ($STANDARDS as $standard) {
    echo '-- ' . $standard . "\n";
    $dir = opendir(LOCATION . '/' . $standard . '/Sniffs');
    while ($sniffdir = readdir($dir)) {
        if (!in_array($sniffdir, array('.', '..'))) {
            echo '-- -- ' . $sniffdir . "\n";
            readSniffs($standard, $sniffdir, $sniffs);
        }
    }
}
ksort($sniffs);

$dynamicRegistry = 'package de.foopara.phpcsmd.option.phpcs;

public class DynamicPhpcsSniffRegistry extends GenericPhpcsSniffRegistry {

    public DynamicPhpcsSniffRegistry() {
';

require_once 'inc.phpcs.sniffs.description.php';

foreach ($sniffs as $standard => $classarr) {
    foreach ($classarr as $class => $sniffarr) {
        foreach ($sniffarr as $sniff) {
            $shortname = strtolower(preg_replace('/\./', '', $sniff));
            $annotationType = 'null';
            if (in_array($sniff, array_keys($ANNOTATIONS))) {
                $annotationType = '"' . $ANNOTATIONS[$sniff] . '"';
            }

            $description = 'null';
            if (in_array($sniff, array_keys($DESCRIPTIONS))) {
                $description = '"' . $DESCRIPTIONS[$sniff] . '"';
            }

            $dynamicRegistry .= '        this.add(new PhpcsSniff("' . $sniff . '", ' . $description . ', ' . $annotationType . '));' . "\n";
        }
    }
}
$dynamicRegistry .= '}}';

file_put_contents('../src/de/foopara/phpcsmd/option/phpcs/DynamicPhpcsSniffRegistry.java', $dynamicRegistry);
