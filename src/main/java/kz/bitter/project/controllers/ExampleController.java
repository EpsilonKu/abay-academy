package kz.bitter.project.controllers;

import kz.bitter.project.entities.Lessons;
import org.commonmark.node.Node;
import org.commonmark.parser.Parser;
import org.commonmark.renderer.html.HtmlRenderer;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;


@Controller
public class ExampleController {
    @GetMapping(value = "/mde-example")
    public String mdeExample(Model model) {
        model.addAttribute("post", new Lessons());
        return "test/simpleMDE-example";
    }

    @PostMapping(value = "/mde-example")
    public String save(Lessons lesson, Model model) {
        lesson.setHtmlContent(markdownToHTML(youtubeConvertor(lesson.getContent())));
        model.addAttribute("post",lesson);
        return "test/viewContent";
    }

    private String markdownToHTML(String markdown) {
        Parser parser = Parser.builder()
                .build();

        Node document = parser.parse(markdown);
        HtmlRenderer renderer = HtmlRenderer.builder()
                .build();

        return renderer.render(document);
    }


    private String youtubeConvertor (String content){
        String newContent = new String();
        while (content.contains("youtube.com")){
            int left = content.indexOf("youtube.com");
            int right = -1;
            for(int i=left;i<content.length();i++){
                right = i;
                if(content.charAt(i) == ' '){
                    break;
                }
            }
            newContent+= insertString(content,"<iframe src=",left-1,right);
            content = splitString(content, right);
            content = insertString(content,"</iframe>",right+1,content.length());
        }
        newContent += content;
        return newContent;
    }

    public String splitString (String content, int index){
        String newString = new String();
        for (int i=index;i<content.length();i++){
            newString += content.charAt(i);
        }
        return newString;
    }


    public static String insertString(
            String originalString,
            String stringToBeInserted,
            int left,int right)
    {
        String newString = new String();

        for (int i = 0; i < right; i++) {

            newString += originalString.charAt(i);

            if (i == left) {
                newString += stringToBeInserted;
            }
        }
        return newString;
    }

}
