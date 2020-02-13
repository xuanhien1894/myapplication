package com.mycompany.myapp.domain;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import com.mycompany.myapp.web.rest.TestUtil;

public class Entity2Test {

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Entity2.class);
        Entity2 entity21 = new Entity2();
        entity21.setId(1L);
        Entity2 entity22 = new Entity2();
        entity22.setId(entity21.getId());
        assertThat(entity21).isEqualTo(entity22);
        entity22.setId(2L);
        assertThat(entity21).isNotEqualTo(entity22);
        entity21.setId(null);
        assertThat(entity21).isNotEqualTo(entity22);
    }
}
