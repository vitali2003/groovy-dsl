import groovy.swing.SwingBuilder
import javax.swing.*

def swing = new SwingBuilder()

def sharedPanel = {
    swing.panel() {
        label("Shared Panel")
    }
}

count = 0
swing.edt {
    frame(title: 'Frame', defaultCloseOperation: JFrame.EXIT_ON_CLOSE, pack: true, show: true) {
        vbox {
            textlabel = label('Click the button!')
            button(
                    text: 'Click Me',
                    actionPerformed: {
                        count++
                        textlabel.text = "Clicked ${count} time(s)."
                        println "Clicked!"
                    }
            )
            widget(sharedPanel())
            widget(sharedPanel())
        }
    }
}