package io.gphotos.gin.lib3.ptp;


import com.raizlabs.android.dbflow.sql.language.Operator;

import io.gphotos.gin.lib3.ptp.PtpConstants.Product;
import io.gphotos.gin.lib3.ptp.PtpConstants.Property;
import java.util.HashMap;
import java.util.Map;
import org.apache.sanselan.formats.jpeg.iptc.IPTCConstants;

public class PtpPropertyHelper {
    public static final int EOS_SHUTTER_SPEED_BULB = 12;
    private static final Map<Integer, String> eosApertureValueMap = new HashMap();
    private static final Map<Integer, Integer> eosDriveModeIconsMap = new HashMap();
    private static final Map<Integer, String> eosDriveModeMap = new HashMap();
    private static final Map<Integer, String> eosFocusModeMap = new HashMap();
    private static final Map<Integer, String> eosIsoSpeedMap = new HashMap();
    private static final Map<Integer, Integer> eosMeteringModeIconsMap = new HashMap();
    private static final Map<Integer, String> eosPictureStyleMap = new HashMap();
    private static final Map<Integer, Integer> eosShootingModeIconsMap = new HashMap();
    private static final Map<Integer, String> eosShootingModeMap = new HashMap();
    private static final Map<Integer, String> eosShutterSpeedMap = new HashMap();
    private static final Map<Integer, Integer> eosWhitebalanceIconsMap = new HashMap();
    private static final Map<Integer, String> eosWhitebalanceMap = new HashMap();
    private static final Map<Integer, String> nikonActivePicCtrlItemMap = new HashMap();
    private static final Map<Integer, String> nikonExposureIndexMap = new HashMap();
    private static final Map<Integer, Integer> nikonExposureProgramMap = new HashMap();
    private static final Map<Integer, Integer> nikonFocusMeteringModeIconsMap = new HashMap();
    private static final Map<Integer, String> nikonFocusMeteringModeMap = new HashMap();
    private static final Map<Integer, String> nikonFocusModeMap = new HashMap();
    private static final Map<Integer, Integer> nikonMeteringModeMap = new HashMap();
    private static final Map<Integer, String> nikonWbColorTempD200Map = new HashMap();
    private static final Map<Integer, String> nikonWbColorTempD300SMap = new HashMap();
    private static final Map<Integer, Integer> nikonWhitebalanceIconsMap = new HashMap();
    private static final Map<Integer, String> nikonWhitebalanceMap = new HashMap();

    public static String getBiggestValue(int i) {
        if (i == Property.FNumber) {
            return "33.3";
        }
        if (i == Property.ExposureTime) {
            return "1/10000";
        }
        if (i == Property.ExposureIndex) {
            return "LO-0.3";
        }
        switch (i) {
            case Property.EosApertureValue /*53505*/:
                return "f 9.5";
            case Property.EosShutterSpeed /*53506*/:
                return "1/8000";
            case Property.EosIsoSpeed /*53507*/:
                return "102400";
            default:
                return "";
        }
    }

    public static Integer mapToDrawable(int i, int i2) {
        return null;
    }

    public static String mapToString(int i, int i2, int i3) {
        char c = '+';
        switch (i2) {
            case Property.ExposureIndex /*20495*/:
                return getNikonExposureIndex(i, i3);
            case Property.ExposureBiasCompensation /*20496*/:
                i = Math.round(((float) Math.abs(i3)) / 100.0f);
                i2 = i / 10;
                i %= 10;
                if (i3 < 0) {
                    c = '-';
                }
                return String.format("%c%d.%d", new Object[]{Character.valueOf(c), Integer.valueOf(i2), Integer.valueOf(i)});
            default:
                StringBuilder stringBuilder;
                StringBuilder stringBuilder2;
                switch (i2) {
                    case Property.NikonShutterSpeed /*53504*/:
                        i = (i3 >> 16) & 65535;
                        i2 = 65535 & i3;
                        if (i2 == 1) {
                            stringBuilder = new StringBuilder();
                            stringBuilder.append("");
                            stringBuilder.append(i);
                            stringBuilder.append("\"");
                            return stringBuilder.toString();
                        } else if (i == 1) {
                            stringBuilder2 = new StringBuilder();
                            stringBuilder2.append("1/");
                            stringBuilder2.append(i2);
                            return stringBuilder2.toString();
                        } else if (i3 == -1) {
                            return "Bulb";
                        } else {
                            if (i3 == -2) {
                                return "Flash";
                            }
                            if (i > i2) {
                                return String.format("%.1f\"", new Object[]{Double.valueOf(((double) i) / ((double) i2))});
                            }
                            StringBuilder stringBuilder3 = new StringBuilder();
                            stringBuilder3.append("");
                            stringBuilder3.append(i);
                            stringBuilder3.append(Operator.Operation.DIVISION);
                            stringBuilder3.append(i2);
                            return stringBuilder3.toString();
                        }
                    case Property.EosApertureValue /*53505*/:
                        Object obj = (String) eosApertureValueMap.get(Integer.valueOf(i3));
                        stringBuilder = new StringBuilder();
                        stringBuilder.append("f ");
                        if (obj == null) {
                            obj = Character.valueOf('?');
                        }
                        stringBuilder.append(obj);
                        return stringBuilder.toString();
                    case Property.EosShutterSpeed /*53506*/:
                        String str = (String) eosShutterSpeedMap.get(Integer.valueOf(i3));
                        if (str == null) {
                            str = Operator.Operation.EMPTY_PARAM;
                        }
                        return str;
                    case Property.EosIsoSpeed /*53507*/:
                        return (String) eosIsoSpeedMap.get(Integer.valueOf(i3));
                    case Property.EosExposureCompensation /*53508*/:
                        if (i3 > 128) {
                            i3 = 256 - i3;
                            c = '-';
                        }
                        if (i3 == 0) {
                            return " 0";
                        }
                        i = i3 / 8;
                        i3 %= 8;
                        String str2 = i3 == 3 ? "1/3" : i3 == 4 ? "1/2" : i3 == 5 ? "2/3" : "";
                        if (i > 0) {
                            return String.format("%c%d %s", new Object[]{Character.valueOf(c), Integer.valueOf(i), str2});
                        }
                        return String.format("%c%s", new Object[]{Character.valueOf(c), str2});
                    case Property.EosShootingMode /*53509*/:
                        return (String) eosShootingModeMap.get(Integer.valueOf(i3));
                    case Property.EosDriveMode /*53510*/:
                        return (String) eosDriveModeMap.get(Integer.valueOf(i3));
                    default:
                        switch (i2) {
                            case PtpConstants.Property.EosAfMode:
                                return (String) eosFocusModeMap.get(Integer.valueOf(i3));
                            case Property.EosWhitebalance /*53513*/:
                                return (String) eosWhitebalanceMap.get(Integer.valueOf(i3));
                            case Property.EosColorTemperature /*53514*/:
                                stringBuilder2 = new StringBuilder();
                                stringBuilder2.append(Integer.toString(i3));
                                stringBuilder2.append("K");
                                return stringBuilder2.toString();
                            default:
                                switch (i2) {
                                    case Property.WhiteBalance /*20485*/:
                                        return (String) nikonWhitebalanceMap.get(Integer.valueOf(i3));
                                    case Property.FNumber /*20487*/:
                                        i = i3 / 100;
                                        i3 %= 100;
                                        if (i3 == 0) {
                                            stringBuilder = new StringBuilder();
                                            stringBuilder.append("f ");
                                            stringBuilder.append(i);
                                            return stringBuilder.toString();
                                        } else if (i3 % 10 == 0) {
                                            stringBuilder = new StringBuilder();
                                            stringBuilder.append("f ");
                                            stringBuilder.append(i);
                                            stringBuilder.append('.');
                                            stringBuilder.append(i3 / 10);
                                            return stringBuilder.toString();
                                        } else {
                                            stringBuilder = new StringBuilder();
                                            stringBuilder.append("f ");
                                            stringBuilder.append(i);
                                            stringBuilder.append('.');
                                            stringBuilder.append(i3);
                                            return stringBuilder.toString();
                                        }
                                    case Property.FocusMode /*20490*/:
                                        return (String) nikonFocusModeMap.get(Integer.valueOf(i3));
                                    case Property.ExposureTime /*20493*/:
                                        if (i3 == -1) {
                                            return "Bulb";
                                        }
                                        i = i3 / IPTCConstants.IMAGE_RESOURCE_BLOCK_PRINT_FLAGS_INFO;
                                        i3 %= IPTCConstants.IMAGE_RESOURCE_BLOCK_PRINT_FLAGS_INFO;
                                        stringBuilder = new StringBuilder();
                                        if (i > 0) {
                                            stringBuilder.append(i);
                                            stringBuilder.append("\"");
                                        }
                                        if (i3 > 0) {
                                            stringBuilder.append("1/");
                                            stringBuilder.append(Math.round(1.0d / (((double) i3) * 1.0E-4d)));
                                        }
                                        return stringBuilder.toString();
                                    case Property.FocusMeteringMode /*20508*/:
                                        return (String) nikonFocusMeteringModeMap.get(Integer.valueOf(i3));
                                    case Property.NikonWbColorTemp /*53278*/:
                                        return getNikonWbColorTemp(i, i3);
                                    case Property.EosPictureStyle /*53520*/:
                                        return (String) eosPictureStyleMap.get(Integer.valueOf(i3));
                                    case Property.NikonExposureIndicateStatus /*53681*/:
                                        stringBuilder2 = new StringBuilder();
                                        stringBuilder2.append("");
                                        stringBuilder2.append(i3 / 6);
                                        stringBuilder2.append(".");
                                        stringBuilder2.append(Math.abs(i3) % 6);
                                        stringBuilder2.append(" EV");
                                        return stringBuilder2.toString();
                                    case Property.NikonActivePicCtrlItem /*53760*/:
                                        return (String) nikonActivePicCtrlItemMap.get(Integer.valueOf(i3));
                                    default:
                                        return Operator.Operation.EMPTY_PARAM;
                                }
                        }
                }
        }
    }

    private static String getNikonExposureIndex(int i, int i2) {
        if (i != 1040 && i != 1042 && i != 1044) {
            if (i != 1050) {
                if (i != 1052) {
                    if (i != Product.NikonD3X) {
                        if (i != 1059) {
                            if (i != 1064) {
                                switch (i) {
                                    case 1061:
                                        break;
                                    case 1062:
                                        if (i2 == 100) {
                                            return "LO-1";
                                        }
                                        if (i2 == 125) {
                                            return "LO-0.7";
                                        }
                                        if (i2 == 140) {
                                            return "LO-0.5";
                                        }
                                        if (i2 == 160) {
                                            return "LO-0.3";
                                        }
                                        if (i2 == 14400) {
                                            return "Hi-0.3";
                                        }
                                        if (i2 == 18000) {
                                            return "Hi-0.5";
                                        }
                                        if (i2 == 20000) {
                                            return "Hi-0.7";
                                        }
                                        if (i2 == 25600) {
                                            return "Hi-1";
                                        }
                                        if (i2 == 51200) {
                                            return "Hi-2";
                                        }
                                        break;
                                }
                            } else if (i2 == 8000) {
                                return "Hi-0.3";
                            } else {
                                if (i2 == 9000) {
                                    return "Hi-0.5";
                                }
                                if (i2 == IPTCConstants.IMAGE_RESOURCE_BLOCK_PRINT_FLAGS_INFO) {
                                    return "Hi-0.7";
                                }
                                if (i2 == 12800) {
                                    return "Hi-1";
                                }
                                if (i2 == 25600) {
                                    return "Hi-2";
                                }
                            }
                        }
                    } else if (i2 == 50) {
                        return "LO-1";
                    } else {
                        if (i2 == 62) {
                            return "LO-0.7";
                        }
                        if (i2 == 70) {
                            return "LO-0.5";
                        }
                        if (i2 == 80) {
                            return "LO-0.3";
                        }
                        if (i2 == 2000) {
                            return "Hi-0.3";
                        }
                        if (i2 == 2240) {
                            return "Hi-0.5";
                        }
                        if (i2 == 2560) {
                            return "Hi-0.7";
                        }
                        if (i2 == 3200) {
                            return "Hi-1";
                        }
                        if (i2 == 6400) {
                            return "Hi-2";
                        }
                    }
                } else if (i2 == 100) {
                    return "LO-1";
                } else {
                    if (i2 == 125) {
                        return "LO-0.7";
                    }
                    if (i2 == 140) {
                        return "LO-0.5";
                    }
                    if (i2 == 160) {
                        return "LO-0.3";
                    }
                    if (i2 == 8320) {
                        return "Hi-0.3";
                    }
                    if (i2 == 8960) {
                        return "Hi-0.5";
                    }
                    if (i2 == 10240) {
                        return "Hi-0.7";
                    }
                    if (i2 == 12800) {
                        return "Hi-1";
                    }
                    if (i2 == 25600) {
                        return "Hi-2";
                    }
                }
            }
            if (i2 == 100) {
                return "LO-1";
            }
            if (i2 == 125) {
                return "LO-0.7";
            }
            if (i2 == 160) {
                return "LO-0.3";
            }
            if (i2 == 4000) {
                return "Hi-0.3";
            }
            if (i2 == 4500) {
                return "Hi-0.5";
            }
            if (i2 == 5000) {
                return "Hi-0.7";
            }
            if (i2 == 6400) {
                return "Hi-1";
            }
        } else if (i2 == 2000) {
            return "Hi-0.3";
        } else {
            if (i2 == 2500) {
                return "Hi-0.7";
            }
            if (i2 == 3200) {
                return "Hi-1";
            }
            if (i2 == 2200) {
                return "Hi-0.5";
            }
        }
        return (String) nikonExposureIndexMap.get(Integer.valueOf(i2));
    }

    private static String getNikonWbColorTemp(int i, int i2) {
        if (i == 1040 || i == 1042) {
            return (String) nikonWbColorTempD200Map.get(Integer.valueOf(i2));
        }
        if (!(i == 1050 || i == 1052 || i == 1064)) {
            switch (i) {
                case Product.NikonD3X /*1056*/:
                case 1057:
                case 1058:
                    break;
                default:
                    switch (i) {
                        case 1061:
                        case 1062:
                            break;
                        default:
                            return null;
                    }
            }
        }
        return (String) nikonWbColorTempD300SMap.get(Integer.valueOf(i2));
    }
}
