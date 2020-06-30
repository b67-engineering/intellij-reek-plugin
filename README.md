# intellij-reek-plugin
[Reek Linter Plugin](https://github.com/troessner/reek) for JetBrains products - IntelliJ and RubyMine.

![Sample file](https://github.com/b67-engineering/intellij-reek-plugin/blob/master/docs/sample-file.png?raw=true)

## Dependencies
[Reek](https://github.com/troessner/reek) needs to be included in `Gemfile` or executable path provided in `Settings > Tools > Reek Linter` after installation.

## Installation
You can install the plugin by opening `Settings > Plugins > Marketplace` and searching for `Reek Linter`.

## Configuration
Plugin should work out-of-the box. 
You can provide path to `.reek.yml` in `Settings > Tools > Reek Linter`.

As mentioned in dependencies - You can provide path to Your `reek` executable or use default loader that uses project SDK / gems provided in `Gemfile`.

### Credentials
Plugin has been inspired by [intellij-protolint](https://github.com/yoheimuta/intellij-protolint).