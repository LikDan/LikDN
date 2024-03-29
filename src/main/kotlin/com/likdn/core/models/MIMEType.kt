package com.likdn.core.models

enum class MIMEType(val extension: String, val kind: String, vararg val mimeType: String) {
    AAC(".aac", "AAC audio", "audio/aac"),
    ABW(".abw", "AbiWord document", "application/x-abiword"),
    ARC(".arc", "Archive document (multiple files embedded)", "application/x-freearc"),
    AVIF(".avif", "AVIF image", "image/avif"),
    AVI(".avi", "AVI: Audio Video Interleave", "video/x-msvideo"),
    AZW(".azw", "Amazon Kindle eBook format", "application/vnd.amazon.ebook"),
    BIN(".bin", "Any kind of binary data", "application/octet-stream"),
    BMP(".bmp", "Windows OS/2 Bitmap Graphics", "image/bmp"),
    BZ(".bz", "BZip archive", "application/x-bzip"),
    BZ2(".bz2", "BZip2 archive", "application/x-bzip2"),
    CDA(".cda", "CD audio", "application/x-cdf"),
    CSH(".csh", "C-Shell script", "application/x-csh"),
    CSS(".css", "Cascading Style Sheets (CSS)", "text/css"),
    CSV(".csv", "Comma-separated values (CSV)", "text/csv"),
    DOC(".doc", "Microsoft Word", "application/msword"),
    DOCX(
        ".docx",
        "Microsoft Word (OpenXML)",
        "application/vnd.openxmlformats-officedocument.wordprocessingml.document"
    ),
    EOT(".eot", "MS Embedded OpenType fonts", "application/vnd.ms-fontobject"),
    EPUB(".epub", "Electronic publication (EPUB)", "application/epub+zip"),
    GZ(".gz", "GZip Compressed Archive", "application/gzip"),
    GIF(".gif", "Graphics Interchange Format (GIF)", "image/gif"),
    HTM(".htm", "HyperText Markup Language (HTML)", "text/html"),
    HTML(".html", "HyperText Markup Language (HTML)", "text/html"),
    ICO(".ico", "Icon format", "image/vnd.microsoft.icon"),
    ICS(".ics", "iCalendar format", "text/calendar"),
    JAR(".jar", "Java Archive (JAR)", "application/java-archive"),
    JPEG(".jpg", "JPEG images", "image/jpeg"),
    JPG(".jpeg, .jpg", "JPEG images", "image/jpeg"),
    JS(".js", "JavaScript", "text/javascript (Specifications: HTML and RFC 9239)"),
    JSON(".json", "JSON format", "application/json"),
    JSONLD(".jsonld", "JSON-LD format", "application/ld+json"),
    MID(".mid", "Musical Instrument Digital Interface (MIDI)", "audio/midi", "audio/x-midi"),
    MIDI(".midi", "Musical Instrument Digital Interface (MIDI)", "audio/midi", "audio/x-midi"),
    MJS(".mjs", "JavaScript module", "text/javascript"),
    MP3(".mp3", "MP3 audio", "audio/mpeg"),
    MP4(".mp4", "MP4 video", "video/mp4"),
    MPEG(".mpeg", "MPEG Video", "video/mpeg"),
    MPKG(".mpkg", "Apple Installer Package", "application/vnd.apple.installer+xml"),
    ODP(".odp", "OpenDocument presentation document", "application/vnd.oasis.opendocument.presentation"),
    ODS(".ods", "OpenDocument spreadsheet document", "application/vnd.oasis.opendocument.spreadsheet"),
    ODT(".odt", "OpenDocument text document", "application/vnd.oasis.opendocument.text"),
    OGA(".oga", "OGG audio", "audio/ogg"),
    OGV(".ogv", "OGG video", "video/ogg"),
    OGX(".ogx", "OGG", "application/ogg"),
    OPUS(".opus", "Opus audio", "audio/opus"),
    OTF(".otf", "OpenType font", "font/otf"),
    PNG(".png", "Portable Network Graphics", "image/png"),
    PDF(".pdf", "Adobe Portable Document Format (PDF)", "application/pdf"),
    PHP(".php", "Hypertext Preprocessor (Personal Home Page)", "application/x-httpd-php"),
    PPT(".ppt", "Microsoft PowerPoint", "application/vnd.ms-powerpoint"),
    PPTX(
        ".pptx",
        "Microsoft PowerPoint (OpenXML)",
        "application/vnd.openxmlformats-officedocument.presentationml.presentation"
    ),
    RAR(".rar", "RAR archive", "application/vnd.rar"),
    RTF(".rtf", "Rich Text Format (RTF)", "application/rtf"),
    SH(".sh", "Bourne shell script", "application/x-sh"),
    SVG(".svg", "Scalable Vector Graphics (SVG)", "image/svg+xml"),
    TAR(".tar", "Tape Archive (TAR)", "application/x-tar"),
    TIF(".tif", "Tagged Image File Format (TIFF)", "image/tiff"),
    TIFF(".tiff", "Tagged Image File Format (TIFF)", "image/tiff"),
    TS(".ts", "MPEG transport stream", "video/mp2t"),
    TTF(".ttf", "TrueType Font", "font/ttf"),
    TXT(".txt", "Text, (generally ASCII or ISO 8859-n)", "text/plain"),
    VSD(".vsd", "Microsoft Visio", "application/vnd.visio"),
    WAV(".wav", "Waveform Audio Format", "audio/wav"),
    WEBA(".weba", "WEBM audio", "audio/webm"),
    WEBM(".webm", "WEBM video", "video/webm"),
    WEBP(".webp", "WEBP image", "image/webp"),
    WOFF(".woff", "Web Open Font Format (WOFF)", "font/woff"),
    WOFF2(".woff2", "Web Open Font Format (WOFF)", "font/woff2"),
    XHTML(".xhtml", "XHTML", "application/xhtml+xml"),
    XLS(".xls", "Microsoft Excel", "application/vnd.ms-excel"),
    XLSX(".xlsx", "Microsoft Excel (OpenXML)", "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"),
    XML(".xml", "XML", "application/xml"),
    XUL(".xul", "XUL", "application/vnd.mozilla.xul+xml"),
    ZIP(".zip", "ZIP archive", "application/zip"),
    _3GP(".3gp", "3GPP audio/video container", "video/3gpp", "audio/3gpp"),
    _3G2(".3g2", "3GPP2 audio/video container", "video/3gpp2", "audio/3gpp2"),
    _7Z(".7z", "7-zip archive", "application/x-7z-compressed"),

    UNKNOWN("", "unknown", ""),
    ALL("", "all", "");

    companion object {
        private val contentTypes: Map<String, MIMEType> = MIMEType.values()
            .flatMap { it.mimeType.toList().map { m -> it to m } }
            .associate { it.second to MIMEType.valueOf(it.first.name) }

        fun getByContentType(contentType: String): MIMEType = contentTypes[contentType] ?: UNKNOWN
    }
}
