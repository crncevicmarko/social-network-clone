package rs.ac.uns.ftn.svtvezbe06.model.entity;

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
@Document(indexName = "group_index")
@Setting(settingPath = "/configuration/serbian-analyzer-config.json")
public class GroupIndex {

    @Id
    private String id;

    @Field(type = FieldType.Integer, store = true, name = "groupId")
    private Integer groupId;
    @Field(type = FieldType.Text, store = true, name = "name", analyzer = "serbian_simple", searchAnalyzer = "serbian_simple")
    private String name;

    @Field(type = FieldType.Text, store = true, name = "description", analyzer = "serbian_simple", searchAnalyzer = "serbian_simple")
    private String description;

    @Field(type = FieldType.Integer, store = true, name = "numberOfPosts")
    private int numberOfPosts;

    @Field(type = FieldType.Text, store = true, name = "rules", analyzer = "serbian_simple", searchAnalyzer = "serbian_simple")
    private String rules;

    @Field(type = FieldType.Integer, store = true, name = "averageLikes")
    private int averageLikes;

    @Field(type = FieldType.Text, store = true, name = "content_sr", analyzer = "serbian_simple", searchAnalyzer = "serbian_simple")
    private String contentSr;

    @Field(type = FieldType.Text, store = true, name = "content_en", analyzer = "english", searchAnalyzer = "english")
    private String contentEn;

    @Field(type = FieldType.Text, store = true, name = "server_filename", index = false)
    private String serverFilename;

    private Map<String, List<String>> highlights;
}
