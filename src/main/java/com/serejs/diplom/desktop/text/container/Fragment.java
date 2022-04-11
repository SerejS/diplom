package com.serejs.diplom.desktop.text.container;

import com.serejs.diplom.desktop.analyze.Analyzer;
import com.serejs.diplom.desktop.enums.AttachmentType;
import lombok.Getter;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

@Getter
public class Fragment {
    private final String content;
    private final Theme theme;
    private final LiteratureType type;
    private final long keyWordsQty;
    private final long countWords;
    private final long length;
    private final Set<Attachment> attachments;

    public Fragment(String content, Theme theme, LiteratureType type, Set<Attachment> attachments) {
        this.content = content;
        this.theme = theme;
        this.type = type;
        this.countWords = this.content.split("\\s").length;
        this.length = this.content.length();
        this.keyWordsQty = Analyzer.countKeyNGrams(this.content, theme.getKeyNGrams());
        this.attachments = new HashSet<>(attachments);
    }

    public float getConcentration() {
        return (float) this.keyWordsQty / (float) this.countWords;
    }

    public void saveAttachments(File dir) {
        if (attachments.isEmpty() || dir == null) return;
        var dirs = new HashMap<AttachmentType, String>();
        dirs.put(AttachmentType.AUDIO, dir.getAbsolutePath() + "audio/");
        dirs.put(AttachmentType.IMAGE, dir.getAbsolutePath() + "image/");
        dirs.put(AttachmentType.TABLE, dir.getAbsolutePath() + "tables.md");

        for (var attachment : attachments) {
            try {
                attachment.save(dirs.get(attachment.type()), dir);
            } catch (IOException ignored) {}
        }
    }
}