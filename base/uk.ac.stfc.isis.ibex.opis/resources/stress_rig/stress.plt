<?xml version="1.0" encoding="UTF-8" standalone="yes"?>
<databrowser>
    <title></title>
    <save_changes>true</save_changes>
    <show_legend>false</show_legend>
    <show_toolbar>false</show_toolbar>
    <grid>false</grid>
    <scroll>true</scroll>
    <update_period>3.0</update_period>
    <scroll_step>5</scroll_step>
    <start>-1 hours 0.0 seconds</start>
    <end>now</end>
    <archive_rescale>STAGGER</archive_rescale>
    <background>
        <red>255</red>
        <green>255</green>
        <blue>255</blue>
    </background>
    <title_font>Segoe UI|12|1</title_font>
    <label_font>Segoe UI|9|1</label_font>
    <scale_font>Segoe UI|9|0</scale_font>
    <legend_font>Segoe UI|9|1</legend_font>
    <axes>
        <axis>
            <visible>true</visible>
            <name>Stress</name>
            <use_axis_name>true</use_axis_name>
            <use_trace_names>false</use_trace_names>
            <right>false</right>
            <color>
                <red>0</red>
                <green>0</green>
                <blue>0</blue>
            </color>
            <min>0.0</min>
            <max>10.0</max>
            <grid>false</grid>
            <autoscale>true</autoscale>
            <log_scale>false</log_scale>
        </axis>
    </axes>
    <annotations>
    </annotations>
    <pvlist>
        <pv>
            <display_name>Stress</display_name>
            <visible>true</visible>
            <name>$(PV_ROOT)STRESS.VAL</name>
            <axis>0</axis>
            <color>
                <red>0</red>
                <green>0</green>
                <blue>0</blue>
            </color>
            <trace_type>AREA</trace_type>
            <linewidth>2</linewidth>
            <point_type>NONE</point_type>
            <point_size>2</point_size>
            <waveform_index>0</waveform_index>
            <period>3.0</period>
            <ring_size>5000</ring_size>
            <request>OPTIMIZED</request>
        </pv>
    </pvlist>
</databrowser>