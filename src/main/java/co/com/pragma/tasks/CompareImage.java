package co.com.pragma.tasks;

import com.github.romankh3.image.comparison.ImageComparison;
import com.github.romankh3.image.comparison.ImageComparisonUtil;
import com.github.romankh3.image.comparison.model.ImageComparisonResult;
import net.serenitybdd.screenplay.Actor;
import net.serenitybdd.screenplay.Task;
import net.serenitybdd.screenplay.Tasks;

import java.awt.image.BufferedImage;
import java.io.File;
import java.util.function.Consumer;

public class CompareImage implements Task {

    private String pathToSave = "src/test/resources/results/result.png";

    private ImageComparison imageComparison;
    private ImageComparisonResult result;
    String actualPath;
    String expectedPath;

    public CompareImage(String actualPath, String expectedPath) {
        this.actualPath = actualPath;
        this.expectedPath = expectedPath;
        BufferedImage expectedImage = ImageComparisonUtil.readImageFromResources(expectedPath);
        BufferedImage actualImage = ImageComparisonUtil.readImageFromResources(actualPath);
        this.imageComparison = new ImageComparison(actualImage,expectedImage);
    }

    @Override
    public <T extends Actor> void performAs(T actor) {
        File resultDestination = new File( pathToSave );
        this.result = imageComparison.compareImages();
        ImageComparisonUtil.saveImage(resultDestination,result.getResult());
        actor.remember("Result Key",result.getImageComparisonState());

    }


    public static CompareImage doComparison(String actualPath, String expectedPath){
        return Tasks.instrumented(CompareImage.class,actualPath,expectedPath);
    }

    public CompareImage configureImageComparison(Consumer<ImageComparison> setter) {
        setter.accept(this.imageComparison);
        return this;
    }
}

