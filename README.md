phpcsmd
=======
[![Build Status](https://travis-ci.org/vralfy/phpcsmd.png?branch=master)](https://travis-ci.org/vralfy/phpcsmd)

Netbeans plugin to report code measurements generated by phpcs, phpmd, phpcpd
and pdepend as tasks and annotations.

Installation
============

 * open Tools -> Plugins and select the "Available Plugins" tab
 * search and install phpcsmd

-----------------------

 * Download and install the latest release from here http://plugins.netbeans.org/plugin/42434

-----------------------

 * check out this repository and open it with Netbeans
 * right click the project and select "Install/Reload in Development IDE"

-----------------------

Usage
=====

 * Open Tools->Options->PHP->PHPCSMD to configure the plugin
 * You can scan files and folders by right clicking on the file/folder or source code and choose "Check for violations" or "Scan with Pdepend"
 * Right click on the PHP-Project and choose Properties -> PHPCSMD to set project specific settings

Tips
====

 * enable "try threading analysis" to prevent Netbeans from freezing during scan
 * enable "update on save" to rescan the file each time you save
 * Open "Action Items" by pressing Ctrl + 6 to get a nice overview of all your violations
 * enter "\.(svn|git)" (or any other regex) to prevent this files/folders from being scanned
 * first configure the path to phpcs, click "show standards" and choose your favorite standard
   you can also enter a path to standard in the "--standard" option if your favorite standard is not installed in pear
 * enable "Use tabs to organize PdependReport" and "Create JDepend statistics" to get a better view on the metrics
 * Deactivate "Create JDepend statics" if you don't care about package abstraction/instability to increase the pdepend performance
 * activate PHPCPD folder scan to detect duplicated source code between your source files
   '''NOTE''': files with violations will be rescanned on save (if activated) until all phpcpd violations have been eliminated.


Thank you
=========

I would like to thank Hugo Fonseca for his suggestions, bug reports and inspirations.
