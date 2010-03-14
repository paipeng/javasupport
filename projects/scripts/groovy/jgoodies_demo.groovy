

// require(url:'jgoodies.com', jar:'animation', version:'1.2.0')
// require(url:'jgoodies.com', jar:'forms', version:'1.1.0')

// Based on the jgoodies animation tutorial class:
//     com.jgoodies.animation.tutorial.intro.BasicTextLabelIntro

import static java.awt.Color.*
import java.awt.Font
import javax.swing.*

import static com.jgoodies.animation.Animations.*
import static com.jgoodies.animation.animations.BasicTextAnimation.defaultFade as fadeText
import static com.jgoodies.animation.animations.BasicTextAnimations.defaultFade as fadeTexts
import com.jgoodies.animation.*
import com.jgoodies.animation.components.BasicTextLabel
import com.jgoodies.forms.builder.PanelBuilder
import com.jgoodies.forms.layout.CellConstraints
import com.jgoodies.forms.layout.FormLayout

class AnimateAction extends AbstractAction {
    def animation
    void actionPerformed(java.awt.event.ActionEvent e) {
        animation.addAnimationListener([
            animationStarted: { ae -> enabled = false },
            animationStopped: { ae -> enabled = true }
        ] as AnimationListener)
        new Animator(animation, 30 /*fps*/).start()
    }
}

def buildPanel(labels) {
    def layout = new FormLayout('fill:pref:grow', 'fill:pref:grow, p, p')
    def builder = new PanelBuilder(layout)
    def cc = new CellConstraints()
    builder.add(buildPreviewPanel(labels), cc.xy(1, 1))
    builder.addSeparator('',               cc.xy(1, 2))
    builder.add(buildToolsPanel(labels),   cc.xy(1, 3))
    return builder.panel
}

def buildPreviewPanel(labels) {
    def layout = new FormLayout('fill:200dlu:grow', 'fill:100dlu:grow')
    def panel = new JPanel(layout)
    def cc = new CellConstraints()
    panel.background = WHITE
    panel.add(labels[0], cc.xy(1, 1))
    panel.add(labels[1], cc.xy(1, 1))
    return panel
}

def buildToolsPanel(labels) {
    def layout = new FormLayout('right:pref:grow', 'pref')
    def builder = new PanelBuilder(layout)
    builder.setDefaultDialogBorder()
    def cc = new CellConstraints()
    def action = new AnimateAction(animation:createAnimation(labels))
    action.putValue('Name', 'Animate')
    builder.add(new JButton(action), cc.xy(1, 1))
    return builder.panel
}

def createAnimation(labels) {
    Animation[] animations = [
        pause(1000),
        fadeText(labels[0], 2500, 'Welcome To', DARK_GRAY),
        pause(1000),
        fadeText(labels[0], 3000, 'JGoodies Animation', DARK_GRAY),
        pause(1000),
        fadeTexts(labels[0], labels[1], 2000, -100,
            'An open source framework|for time-based|' +
            'real-time animations|in Java and Groovy.',
            DARK_GRAY),
        pause(1000),
        fadeTexts(labels[0], labels[1], 3000, 500, 'Main Features:', DARK_GRAY),
        pause(1000),
        fadeTexts(labels[0], labels[1], 1750, 0,
            'Seamless|flexible|and powerful integration|' +
            'with Java and Groovy.|Small library size.',
            DARK_GRAY),
        pause(1500)
    ]
    sequential(animations)
}

def buildLabel(font) {
    def label = new BasicTextLabel(' ')
    label.font = font
    label.opaque = false
    return label
}

def centerOnScreen(component) {
    def paneSize = component.size
    def screenSize = component.toolkit.screenSize
    int x = (screenSize.width - paneSize.width) / 2
    int y = (screenSize.height - paneSize.height) * 0.45
    component.setLocation(x, y)
}

def font = new Font('Tahoma', Font.BOLD, 18)
def label1 = buildLabel(font)
def label2 = buildLabel(font)
def frame = new JFrame()
frame.title = 'Groovy/JGoodies Animation Demo'
frame.defaultCloseOperation = WindowConstants.EXIT_ON_CLOSE
def panel = buildPanel([label1, label2])
frame.contentPane.add(panel)
frame.pack()
centerOnScreen(frame)
frame.visible = true

