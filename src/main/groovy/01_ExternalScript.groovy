new GroovyShell().evaluate(
'''
import com.jeeconf.groovydsl.Monitoring

Monitoring.sendStatusPeriodicallyPeriodically('380934902436', 30000)
'''
)