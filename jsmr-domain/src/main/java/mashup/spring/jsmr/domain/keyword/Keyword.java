package mashup.spring.jsmr.domain.keyword;

import lombok.*;
import mashup.spring.jsmr.domain.BaseEntity;
import mashup.spring.jsmr.domain.profileKeyword.ProfileKeyword;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
@Entity
public class Keyword extends BaseEntity {

    private String keyword;

    @OneToMany(mappedBy = "keyword")
    private List<ProfileKeyword> profileKeywords = new ArrayList<>();
}
