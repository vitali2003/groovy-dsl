String script = new File("../dsl/${this.class.name}.dsl").text
new GroovyShell().evaluate(script)