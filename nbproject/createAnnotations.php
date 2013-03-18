<?php
$RESLOC = 'nbresloc:/de/foopara/phpcsmd/resources/';
$RESOURCEDIR = '../src/de/foopara/phpcsmd/resources';
$BASENAME = 'de-foopara-phpcsmd-annotation-';
$DEFAULT_ANNOTATION = array(
    'name'                 => $BASENAME . 'unknown-unknown',
    'type'                 => 'line',
    'visible'              => 'true',
    'browsable'            => 'true',
    'highlight'            => '#FFFFFF',
    'custom_sidebar_color' => '#FFFFFF',
    'glyph'                => $RESLOC . 'icon.png',
    'severity'                => 'none',

);
$ANNOTATIONS = array(
    'phpcs' => array(
        'error' => array(
            'highlight' => '#FFC0C0',
            'custom_sidebar_color' => '#FFC0C0',
        ),
        'warning' => array(
            'highlight' => '#FFFFCF',
            'custom_sidebar_color' => '#FFFFCF',
        ),
        'comment' => array(
            'highlight' => '#EEEEEE',
            'custom_sidebar_color' => '#EEEEEE',
        ),
        'complex' => array(
            'highlight' => '#2CB7B3',
            'custom_sidebar_color' => '#2CB7B3',
        ),
        'forbidden' => array(
            'highlight' => '#FF9090',
            'custom_sidebar_color' => '#FF9090',
        ),
        'unreachable' => array(
            'highlight' => '#CCCCCC',
            'custom_sidebar_color' => '#CCCCCC',
        ),
    ),
    'phpmd' => array(
        'violation' => array(
            'highlight' => '#FFC0FF',
            'custom_sidebar_color' => '#FFC0FF',
        ),
        'complex' => array(
            'highlight' => '#2CB7B3',
            'custom_sidebar_color' => '#2CB7B3',
        ),
    ),
    'phpcpd' => array(
        'violation' => array(
            'highlight' => '#00FFFF',
            'custom_sidebar_color' => '#00FFFF',
        ),
    ),
);

$layerSettings = '';
foreach ($ANNOTATIONS as $tool => $violations) {
    foreach ($violations as $violation => $settings) {
        $generatedSettings = array(
            'name'  => $BASENAME . $tool . '-' . $violation,
            'glyph' => $RESLOC . $tool . '/' . $violation . '.png',
        );
        $s = array_merge($DEFAULT_ANNOTATION, $generatedSettings, $settings);
        // Main File
        $res = createTypeStr($s);
        $file = realpath(getcwd() . '/' . $RESOURCEDIR . '/' . $tool)
                . DIRECTORY_SEPARATOR . $violation . '.xml';
        $layerSettings .= '            <file name="' . $BASENAME . $tool . '-' . $violation . '.xml" '
                . 'url="' . $RESLOC . $tool . '/' . $violation . '.xml"/>' . "\n";
//        echo $res . "\n" . $file . "\n";
        file_put_contents($file, $res);

        // ErrorStripe File
        $s['severity'] = 'warning';
        $s['name']     = $s['name'] . '-severity';
        $res = createTypeStr($s);
        $file = realpath(getcwd() . '/' . $RESOURCEDIR . '/' . $tool)
                . DIRECTORY_SEPARATOR . $violation . '-severity.xml';
        $layerSettings .= '            <file name="' . $BASENAME . $tool . '-' . $violation . '-severity.xml" '
                . 'url="' . $RESLOC . $tool . '/' . $violation . '-severity.xml"/>' . "\n";
//        echo $res . "\n" . $file . "\n";
        file_put_contents($file, $res);
    }
}

$xmlLayerFile = realpath(getcwd() . '/../src/de/foopara/phpcsmd/layer.xml');
$xmlLayer = file_get_contents($xmlLayerFile);
$xmlLayer = preg_replace('/<!-- BEGIN GENERATED ANNOTATIONS -->.*<!-- END GENERATED ANNOTATIONS -->/s',
        '<!-- BEGIN GENERATED ANNOTATIONS -->' . "\n" . $layerSettings . '            <!-- END GENERATED ANNOTATIONS -->',
        $xmlLayer);
file_put_contents($xmlLayerFile, $xmlLayer);

function createTypeStr($s) {
    $res = '<?xml version="1.0" encoding="UTF-8"?>' . "\n";
    $res.= '<!DOCTYPE type PUBLIC "-//NetBeans//DTD annotation type 1.0//EN" "http://www.netbeans.org/dtds/annotation-type-1_0.dtd">' . "\n";
    $res.= '<type' . "\n";
    foreach ($s as $key => $value) {
        $res .= '      ' . $key . '="' . $value . '"' . "\n";
    }
    $res .= '/>';

    return $res;
}