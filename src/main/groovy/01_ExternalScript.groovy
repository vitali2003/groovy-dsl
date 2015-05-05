new GroovyShell().evaluate(
'''
import com.jeeconf.groovydsl.Monitoring

Monitoring.sendStatusPeriodically('380934902436', 30000)
'''
)