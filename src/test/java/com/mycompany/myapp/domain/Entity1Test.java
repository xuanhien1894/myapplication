package com.mycompany.myapp.domain;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import com.mycompany.myapp.web.rest.TestUtil;

public class Entity1Test {

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Entity1.class);
        Entity1 entity11 = new Entity1();
        entity11.setId(1L);
        Entity1 entity12 = new Entity1();
        entity12.setId(entity11.getId());
        assertThat(entity11).isEqualTo(entity12);
        entity12.setId(2L);
        assertThat(entity11).isNotEqualTo(entity12);
        entity11.setId(null);
        assertThat(entity11).isNotEqualTo(entity12);
    }
}
