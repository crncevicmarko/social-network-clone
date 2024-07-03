package rs.ac.uns.ftn.svtvezbe06.model.entity;

//import jakarta.persistence.Id;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;
import org.springframework.data.elasticsearch.annotations.Setting;

import javax.persistence.Id;
import java.util.List;
import java.util.Map;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Document(indexName = "post_index")
@Setting(settingPath = "/configuration/serbian-analyzer-config.json")
public class PostIndex {

    @Id
    private String id;

    @Field(type = FieldType.Integer, store = true, name = "postId")
    private int postId;

    @Field(type = FieldType.Text, store = true, name = "content", analyzer = "serbian_simple", searchAnalyzer = "serbian_simple")
    private String content;

    @Field(type = FieldType.Integer, store = true, name = "numberOfLikes")
    private int numberOfLikes;

    @Field(type = FieldType.Text, store = true, name = "commentContent", analyzer = "serbian_simple", searchAnalyzer = "serbian_simple")
    private String commentsContent;

    @Field(type = FieldType.Integer, store = true, name = "numberOfComments")
    private int numberOfComments;

    @Field(type = FieldType.Text, store = true, name = "content_sr", analyzer = "serbian_simple", searchAnalyzer = "serbian_simple")
    private String contentSr;

    @Field(type = FieldType.Text, store = true, name = "content_en", analyzer = "english", searchAnalyzer = "english")
    private String contentEn;

    @Field(type = FieldType.Text, store = true, name = "server_filename", index = false)
    private String serverFilename;

    private Map<String, List<String>> highlights;
}
