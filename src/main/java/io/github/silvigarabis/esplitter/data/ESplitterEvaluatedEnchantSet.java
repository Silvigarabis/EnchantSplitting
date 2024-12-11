package io.github.silvigarabis.esplitter.data;

import org.bukkit.enchantments.Enchantment;

import java.util.LinkedHashSet;

public class ESplitterEvaluatedEnchantSet {
    public LinkedHashSet<Enchantment> enchantSet;
    public ESplitterConsumpsion consumpsion;
    public float successRate = 1;

    public ESplitterEvaluatedEnchantSet clone() {
        var copy = new ESplitterEvaluatedEnchantSet();
        copy.enchantSet = new LinkedHashSet<>(this.enchantSet);
        copy.consumpsion = this.consumpsion.clone();
        copy.successRate = this.successRate;
        return copy;
    }

    public static ESplitterEvaluatedEnchantSet cloneIt(ESplitterEvaluatedEnchantSet raw) {
        return raw.clone();
    }
}
