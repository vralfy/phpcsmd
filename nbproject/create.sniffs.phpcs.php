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
            $res[$standard][$sniffclass][$sniffName]['name'] = $sniffName;
            readKeys($standard, $sniffclass, $sniffName,
                    LOCATION . '/' . $standard . '/Sniffs/' . $sniffclass . '/' . $sniff,
                    $res);
        }
    }
    asort($res[$standard][$sniffclass]);
    ksort($res[$standard]);
}

function readKeys($standard, $sniffclass, $sniffName, $file, &$res) {
    $file = file_get_contents($file);
    /** Got regex from https://github.com/potherca/PhpCodeSnifferKeyFinder/ **/
    $sRegexp = '!                                                               # START PATTERN
                (?:->\s*                                                        # open NCS (non-capturing subpattern)
                    add(?P<SEVERITY>Warning|Error)                              # Method name
                )                                                               # close NCS
                \s*\(                                                           # (
                                                                                # START PARAMETERS
                    (?|(?P<MESSAGE>\$[a-z0-9_]+)|[\'"](?P<MESSAGE>.*?)[\'"])    # message OR name of the variable containing the message
                    \s*,\s*                                                     # ,
                    (.*?)                                                       # unneeded parameter
                    \s*,\s*                                                     # ,
                    (?|(?P<KEY>\$[a-z0-9_]+)|[\'"](?P<KEY>.*?)[\'"])            # key OR name of the variable containing the key
                    .*                                                          # unneeded parameter
                                                                                # END PARAMETERS
                \)\s*;                                                          # )
                !xm';
    $aKeys = array();
    if(preg_match_all($sRegexp, $file, $matches, PREG_SET_ORDER) > 0) {
        foreach ($matches as $match) {
            $sKey = $match['KEY'];
            if ($sKey{0} === '$') {
                $sFullMatch = $match[0];
                $fileLines = explode("\n", $file);
                foreach ($fileLines as $lineNumber => $lineContent) {
                    if (strpos($lineContent, $sFullMatch) !== false) {
                        $iLineNumber = $lineNumber;
                        break;
                    }
                }

                if (isset($iLineNumber)) {
                    // Lets look for that variable!
                    while ($iLineNumber > -1) { // fallback in case we don't find it
                        $iLineNumber--;
                        $sLine = $fileLines[$iLineNumber];

                        if (preg_match('/function [a-zA-Z_\x7f-\xff][a-zA-Z0-9_\x7f-\xff]*\s*\(.*?/ms', $sLine, $aMatches) > 0) {
                            // We really don't want to keep parsing beyond the boundary of the current function
                            break;
                        } else if (preg_match('#\\' . $sKey . '\s*=\s*(\'|")(?P<KEY>.*?)\1#', $sLine, $aMatches) > 0) {
                            $sKey = $aMatches['KEY'];
                            break;
                        }
                    }
                }
            }
            echo '-- -- -- -- ' . $sKey . "\n";
            $aKeys[] = $sKey;
        }
    }
    $res[$standard][$sniffclass][$sniffName]['keys'] = $aKeys;
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
        PhpcsKeys keys = null;
';

require_once 'inc.phpcs.sniffs.description.php';

foreach ($sniffs as $standard => $classarr) {
    foreach ($classarr as $class => $sniffarr) {
        foreach ($sniffarr as $sniff) {
            $sniffPath = $sniff['name'];
            if (empty($sniffPath)) {
                var_dump($sniff); die;
            }
            $shortname = strtolower(preg_replace('/\./', '', $sniffPath));
            $annotationType = 'null';
            if (in_array($sniffPath, array_keys($ANNOTATIONS))) {
                $annotationType = '"' . $ANNOTATIONS[$sniffPath] . '"';
            }

            $description = 'null';
            if (in_array($sniffPath, array_keys($DESCRIPTIONS))) {
                $description = '"' . $DESCRIPTIONS[$sniffPath] . '"';
            }

            $sniffKeys = 'null';
            if (!empty($sniff['keys'])) {
                $sniffKeys = 'keys';
                $dynamicRegistry .= '        keys = new PhpcsKeys();' . "\n";
                foreach ($sniff['keys'] as $key) {
                    $dynamicRegistry .= '        keys.addKey("' . $key . '");' . "\n";
                }
            }

            $dynamicRegistry .= '        this.add(new PhpcsSniff("' . $sniffPath . '", ' . $description . ', ' . $annotationType . ', ' . $sniffKeys . '));' . "\n";

        }
        $dynamicRegistry .= '//      --------------------------------------' . "\n";
    }
}
$dynamicRegistry .= '}}';

file_put_contents('../src/de/foopara/phpcsmd/option/phpcs/DynamicPhpcsSniffRegistry.java', $dynamicRegistry);
