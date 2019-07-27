



```groovy
def file = new File(basedir, 'build.log')

def correct = false

file.eachLine {
    if (it =~ /.检验通过*/) {
        correct = true
    }
}

if (!correct) {
    throw new RuntimeException('正确的项目配置检验未通过')
}
```

