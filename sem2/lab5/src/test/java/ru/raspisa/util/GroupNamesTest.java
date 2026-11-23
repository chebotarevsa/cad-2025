package ru.raspisa.util;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class GroupNamesTest {

    @Test
    void normalize_trimsAndUppercasesNames() {
        assertThat(GroupNames.normalize("  вр-1 ")).isEqualTo("ВР-1");
    }

    @Test
    void comparator_sortsNumericPartsNaturally() {
        List<String> groups = new ArrayList<>(List.of("ВР-10", "10", "ВР-2", "2", "ВР-1"));

        groups.sort(GroupNames.COMPARATOR);

        assertThat(groups).containsExactly("2", "10", "ВР-1", "ВР-2", "ВР-10");
    }
}
