import java.util.concurrent.Executors

import static java.awt.RenderingHints.*
import java.awt.image.BufferedImage
import javax.imageio.ImageIO

def negativeImagesUrl = "http://image-net.org/api/text/imagenet.synset.geturls?wnid=n04105893" 
def negativeImagesWidth = 100
def negativeImagesHeight = 100
def positiveImagesWidth = 50
def positiveImagesHeight = 50
def workSpaceDir = "/opencv_workspace" // keep neg al last folder !!!
   
new File(workSpaceDir).mkdirs()
new File(workSpaceDir + "/neg").mkdirs()
new File(workSpaceDir + "/pos").mkdirs()

resizePosImages(positiveImagesWidth, positiveImagesHeight, workSpaceDir)
createInfoFile(workSpaceDir, positiveImagesWidth, positiveImagesHeight)
// downloadNegativeImagesAndResize(negativeImagesUrl, workSpaceDir, negativeImagesWidth, negativeImagesHeight)
resizeNegImages(negativeImagesWidth, negativeImagesHeight, workSpaceDir)
createBgFile(workSpaceDir)


println "### Next steps ###"
println "# Samples von positiven Bilder generieren"
println "opencv_createsamples -img pos/nerf.jpg \\"
println "   -bg bg.txt \\"
println "   -info info/info.lst \\"
println "   -pngoutput info \\"
println "   -maxxangle 0.5 \\"
println "   -maxyangle 0.5 \\"
println "   -maxzangle 0.5 \\"
println "   -num 1300"


println "# Samples von positiven Bilder generieren"
println "opencv_createsamples -info info/info.lst \\"
println "   -num 1300 \\"
println "   -w 20 \\"
println "   -h 20 \\"
println "   -vec positives.vec"


println "# Trainieren"
println "opencv_traincascade \\"
println "   -data data \\"
println "   -vec positives.vec \\"
println "   -bg bg.txt \\"
println "   -numPos 1200 \\"
println "   -numNeg 600 \\"
println "   -numStages 10 \\"
println "   -w 20 \\"
println "   -h 20"


def List downloadNegativeImagesAndResize(String negativeImagesUrl, String workSpaceDir, int negativeImagesWidth, int negativeImagesHeight) {
    List runnables = []
    forEachLink(negativeImagesUrl) { link ->
        runnables.add({
            try {
                def uuid = UUID.randomUUID().toString()
                def fileName = workSpaceDir + "/neg/nok-${uuid}.jpg"
                println "[+] download: ${link}"
                downloadFile(link, fileName)   
                println "[+] resize: ${fileName} with: ${negativeImagesWidth} height: ${negativeImagesHeight}"
                resize(fileName, fileName, negativeImagesWidth, negativeImagesHeight, true)
            } catch (Exception e) {
                println "[!] downloadFile failed: ${e} for ${link}"
            }
        }) 
    }
    def pool = Executors.newFixedThreadPool(10)
    pool.invokeAll(runnables)
    pool.shutdown()
}

def resizePosImages(int objectWidth, int objectHeight, String workSpaceDir) {
    println "[+] resize pos images"
    new File(workSpaceDir + "/pos").listFiles()
        .each { f -> resize(f.getPath(), f.getPath(), objectWidth, objectHeight) }
}

def resizeNegImages(int objectWidth, int objectHeight, String workSpaceDir) {
    println "[+] resize neg images"
    new File(workSpaceDir + "/neg").listFiles()
        .each { f -> resize(f.getPath(), f.getPath(), objectWidth, objectHeight) }
}

def forEachLink(String imagesUrl, Closure closure) {
    def links = imagesUrl.toURL().getText()
    links.split("\r\n").each {
        closure(it)
    }
}

def downloadFile(String link, String outputFile, int timeout=6000) {
    def out = new File(outputFile)
    link.toURL().openConnection().with {
        readTimeout = timeout
        if (responseCode != 200)
            return;
        out << inputStream
    }
}

def resize(String originalFile, String newFile, int newWidth, int newHeight, boolean grayScale=false) {
    def img = ImageIO.read( new File(originalFile))
    def imgType = grayScale ? BufferedImage.TYPE_BYTE_GRAY : img.type
    new BufferedImage( newWidth, newHeight, imgType ).with { i ->
        createGraphics().with {
            setRenderingHint( KEY_INTERPOLATION, VALUE_INTERPOLATION_BICUBIC )
            drawImage( img, 0, 0, newWidth, newHeight, null )
            dispose()
        }
        ImageIO.write( i, newFile.replaceAll("^.+\\.([a-zA-Z1-9]+)\$", "\$1"), new File(newFile) )
    }
}

def createBgFile(String workSpaceDir) {
    def bgTxt = new File(workSpaceDir + "/bg.txt")
    println "[+] create description ${bgTxt}"
    new File(workSpaceDir + "/neg").listFiles()
        .each { f -> bgTxt << "neg/${f.name}\n" }
}

def createInfoFile(String workSpaceDir, int objectWidth, int objectHeight) {
    def bgTxt = new File(workSpaceDir + "/info.dat")
    println "[+] create description ${bgTxt}"
    new File(workSpaceDir + "/pos").listFiles()
        .each { f -> bgTxt << "pos/${f.name} 1 0 0 ${objectWidth} ${objectHeight}\n" }
}