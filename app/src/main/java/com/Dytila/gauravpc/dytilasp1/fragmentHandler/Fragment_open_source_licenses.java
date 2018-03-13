package com.Dytila.gauravpc.dytilasp1.fragmentHandler;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.Dytila.gauravpc.dytilasp1.R;

/**
 Created by gaurav pc on 15-Jan-17.
 */
public class Fragment_open_source_licenses extends Fragment {

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v=inflater.inflate(R.layout.fragment_open_source_licenses, null);

        TextView t12_text,t22_text,t32_text,t42_text,t52_text;

        t12_text=(TextView)v.findViewById(R.id.t12);
        t12_text.setText("Copyright 2011-2015 Sergey Tarasevich\n" +
                "\n" +
                "Licensed under the Apache License, Version 2.0 (the \"License\");\n" +
                "you may not use this file except in compliance with the License.\n" +
                "You may obtain a copy of the License at\n" +
                "\n" +
                "   http://www.apache.org/licenses/LICENSE-2.0\n" +
                "\n" +
                "Unless required by applicable law or agreed to in writing, software\n" +
                "distributed under the License is distributed on an \"AS IS\" BASIS,\n" +
                "WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.\n" +
                "See the License for the specific language governing permissions and\n" +
                "limitations under the License.");

        t22_text=(TextView)v.findViewById(R.id.t22);
        t22_text.setText("Copyright 2016 The Android Open Source Project, Inc.\n" +
                "\n" +
                "Licensed to the Apache Software Foundation (ASF) under one or more contributor license agreements. See the NOTICE file distributed with this work for additional information regarding copyright ownership. The ASF licenses this file to you under the Apache License, Version 2.0 (the \"License\"); you may not use this file except in compliance with the License. You may obtain a copy of the License at\n" +
                "\n" +
                "http://www.apache.org/licenses/LICENSE-2.0\n" +
                "\n" +
                "Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an \"AS IS\" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the specific language governing permissions and limitations under the License.");
        t32_text=(TextView)v.findViewById(R.id.t32);
        t32_text.setText("    Copyright (C) 2012 The Android Open Source Project\n" +
                "         Licensed under the Apache License, Version 2.0 (the \"License\");\n" +
                "        you may not use this file except in compliance with the License.\n" +
                "        You may obtain a copy of the License at\n\n" +
                "             http://www.apache.org/licenses/LICENSE-2.0\n\n" +
                "        Unless required by applicable law or agreed to in writing, software\n" +
                "        distributed under the License is distributed on an \"AS IS\" BASIS,\n" +
                "        WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.\n" +
                "        See the License for the specific language governing permissions and\n" +
                "        limitations under the License.");

        t42_text=(TextView)v.findViewById(R.id.t42);
        t42_text.setText("Copyright 2015 jack wang\n" +
                "\n" +
                "Licensed under the Apache License, Version 2.0 (the \"License\");\n" +
                "you may not use this file except in compliance with the License.\n" +
                "You may obtain a copy of the License at\n" +
                "\n" +
                "   http://www.apache.org/licenses/LICENSE-2.0\n" +
                "\n" +
                "Unless required by applicable law or agreed to in writing, software\n" +
                "distributed under the License is distributed on an \"AS IS\" BASIS,\n" +
                "WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.\n" +
                "See the License for the specific language governing permissions and\n" +
                "limitations under the License.");

        t52_text=(TextView)v.findViewById(R.id.t52);
        t52_text.setText("Copyright 2014 - 2016 Henning Dodenhof\n" +
                "\n" +
                "Licensed under the Apache License, Version 2.0 (the \"License\");\n" +
                "you may not use this file except in compliance with the License.\n" +
                "You may obtain a copy of the License at\n" +
                "\n" +
                "    http://www.apache.org/licenses/LICENSE-2.0\n" +
                "\n" +
                "Unless required by applicable law or agreed to in writing, software\n" +
                "distributed under the License is distributed on an \"AS IS\" BASIS,\n" +
                "WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.\n" +
                "See the License for the specific language governing permissions and\n" +
                "limitations under the License.");

        return v;
    }
}

