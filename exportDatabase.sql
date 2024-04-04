PGDMP                         |            pokemon    15.6    15.6                0    0    ENCODING    ENCODING        SET client_encoding = 'UTF8';
                      false                       0    0 
   STDSTRINGS 
   STDSTRINGS     (   SET standard_conforming_strings = 'on';
                      false                       0    0 
   SEARCHPATH 
   SEARCHPATH     8   SELECT pg_catalog.set_config('search_path', '', false);
                      false                       1262    16398    pokemon    DATABASE     ~   CREATE DATABASE pokemon WITH TEMPLATE = template0 ENCODING = 'UTF8' LOCALE_PROVIDER = libc LOCALE = 'Portuguese_Brazil.1252';
    DROP DATABASE pokemon;
                postgres    false            �            1259    16407    next_evolutions    TABLE     �   CREATE TABLE public.next_evolutions (
    id integer NOT NULL,
    num integer NOT NULL,
    name character varying(64) NOT NULL,
    fk_pokemon integer
);
 #   DROP TABLE public.next_evolutions;
       public         heap    postgres    false            �            1259    16406    evolutions_id_seq    SEQUENCE     �   CREATE SEQUENCE public.evolutions_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;
 (   DROP SEQUENCE public.evolutions_id_seq;
       public          postgres    false    217                       0    0    evolutions_id_seq    SEQUENCE OWNED BY     L   ALTER SEQUENCE public.evolutions_id_seq OWNED BY public.next_evolutions.id;
          public          postgres    false    216            �            1259    16400    pokemons    TABLE     �   CREATE TABLE public.pokemons (
    id integer NOT NULL,
    num integer NOT NULL,
    name character varying(64) NOT NULL,
    type character varying(64) NOT NULL
);
    DROP TABLE public.pokemons;
       public         heap    postgres    false            �            1259    16399    pokemons_id_seq    SEQUENCE     �   CREATE SEQUENCE public.pokemons_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;
 &   DROP SEQUENCE public.pokemons_id_seq;
       public          postgres    false    215                       0    0    pokemons_id_seq    SEQUENCE OWNED BY     C   ALTER SEQUENCE public.pokemons_id_seq OWNED BY public.pokemons.id;
          public          postgres    false    214            �            1259    24581    prev_evolutions    TABLE     �   CREATE TABLE public.prev_evolutions (
    id integer DEFAULT nextval('public.pokemons_id_seq'::regclass) NOT NULL,
    num integer NOT NULL,
    name character varying(64) NOT NULL,
    fk_pokemon integer
);
 #   DROP TABLE public.prev_evolutions;
       public         heap    postgres    false    214            o           2604    16410    next_evolutions id    DEFAULT     s   ALTER TABLE ONLY public.next_evolutions ALTER COLUMN id SET DEFAULT nextval('public.evolutions_id_seq'::regclass);
 A   ALTER TABLE public.next_evolutions ALTER COLUMN id DROP DEFAULT;
       public          postgres    false    217    216    217            n           2604    16403    pokemons id    DEFAULT     j   ALTER TABLE ONLY public.pokemons ALTER COLUMN id SET DEFAULT nextval('public.pokemons_id_seq'::regclass);
 :   ALTER TABLE public.pokemons ALTER COLUMN id DROP DEFAULT;
       public          postgres    false    214    215    215                      0    16407    next_evolutions 
   TABLE DATA           D   COPY public.next_evolutions (id, num, name, fk_pokemon) FROM stdin;
    public          postgres    false    217   c       
          0    16400    pokemons 
   TABLE DATA           7   COPY public.pokemons (id, num, name, type) FROM stdin;
    public          postgres    false    215   /                 0    24581    prev_evolutions 
   TABLE DATA           D   COPY public.prev_evolutions (id, num, name, fk_pokemon) FROM stdin;
    public          postgres    false    218   %                  0    0    evolutions_id_seq    SEQUENCE SET     B   SELECT pg_catalog.setval('public.evolutions_id_seq', 1765, true);
          public          postgres    false    216                       0    0    pokemons_id_seq    SEQUENCE SET     @   SELECT pg_catalog.setval('public.pokemons_id_seq', 1938, true);
          public          postgres    false    214            v           2606    16412 $   next_evolutions next_evolutions_pkey 
   CONSTRAINT     b   ALTER TABLE ONLY public.next_evolutions
    ADD CONSTRAINT next_evolutions_pkey PRIMARY KEY (id);
 N   ALTER TABLE ONLY public.next_evolutions DROP CONSTRAINT next_evolutions_pkey;
       public            postgres    false    217            r           2606    16405    pokemons pokemons_pkey 
   CONSTRAINT     T   ALTER TABLE ONLY public.pokemons
    ADD CONSTRAINT pokemons_pkey PRIMARY KEY (id);
 @   ALTER TABLE ONLY public.pokemons DROP CONSTRAINT pokemons_pkey;
       public            postgres    false    215            x           2606    24585 $   prev_evolutions prev_evolutions_pkey 
   CONSTRAINT     b   ALTER TABLE ONLY public.prev_evolutions
    ADD CONSTRAINT prev_evolutions_pkey PRIMARY KEY (id);
 N   ALTER TABLE ONLY public.prev_evolutions DROP CONSTRAINT prev_evolutions_pkey;
       public            postgres    false    218            t           2606    24593    pokemons unique_num 
   CONSTRAINT     M   ALTER TABLE ONLY public.pokemons
    ADD CONSTRAINT unique_num UNIQUE (num);
 =   ALTER TABLE ONLY public.pokemons DROP CONSTRAINT unique_num;
       public            postgres    false    215            y           2606    24576    next_evolutions fk_pokemon    FK CONSTRAINT     �   ALTER TABLE ONLY public.next_evolutions
    ADD CONSTRAINT fk_pokemon FOREIGN KEY (fk_pokemon) REFERENCES public.pokemons(id) NOT VALID;
 D   ALTER TABLE ONLY public.next_evolutions DROP CONSTRAINT fk_pokemon;
       public          postgres    false    3186    215    217            z           2606    24586    prev_evolutions fk_pokemon    FK CONSTRAINT     �   ALTER TABLE ONLY public.prev_evolutions
    ADD CONSTRAINT fk_pokemon FOREIGN KEY (fk_pokemon) REFERENCES public.pokemons(id) NOT VALID;
 D   ALTER TABLE ONLY public.prev_evolutions DROP CONSTRAINT fk_pokemon;
       public          postgres    false    3186    215    218               �  x�e��r�6���I	���z��M\N\v�{�a�e�P r����ڕ|S}"�����r��F~��s�B8S�mE�}�#�`�X��y�6)���/�Uܿ���:���/�c�r����� �C�O�����6�V���4�]汪%t�9��)���ҁ��'���\��y�`�&����ԕ~�iX�RC��Kn�n��4eb�Q��>��R#�Bn�ԯ�Ĳ45V�F��X�K��Oe���	�4/���LV+��x��c��bb
����w���U��1MѪ���V�"nD��3ثu�Z�����jNiKjE�Cz��:uא:�ώ���Ė�����z=<O���'�b���2Rƶ���r�A�V-�X��m:`��2��`���}Z�XO!�h������ԈUr1���Sφa��&�}���N�e�ݼ��ۅa,�*�6�� /6`��8BR���8%7y�6}������2�y�%��7�>`^+9����	9�F���i���%nԜRV�Xq�\��&?�4����|�q�;��Rr��� S���2qy��:M�Va�	�?�9�z�N�l����)����)�n���%�@R�"�}��9B�.+��۸뻺-^�b�av7�eɄ�F��Ժ�k����M-���"w���׃ p.��:������H��>�uˌ���!?�Z����6��[��7Hs�"��u���1�wXA&|{�q=���9��_a	_�q�]�!��� ���q��j�!���i����-�%P��k�Qdug,"Q������&�j(l��湫*y*b���]�]�`� ���f�,�D���	����V�5R���36��Ȉ��y����P�(�Z��d����+Ew���!=��0�����P�P=��t��a��
�eT�D�6�|�1�o�g��q����~;;;��&��      
   �  x�mW�r�6]�|Evmg<�O`iǱӸJ<Qƙ�t���D� �^u���~IH�)+[� ��\�TbtS��,e���ɲ�z����+������\H=���)�PLo���e�+Gw�)��t�2��)ma�"]ޢWw���$c���Z��(�*+�<�W�9��#�
;������_�o�0'��P�<ؼ���M]��g��Gǭ����*����c§�.��K��8m������j�E����[�:�V���CB=�&�}�U%+ٳN	!�k$c�s
-J:{�\LP���ɘ��n'����␐�k���	S�`�;����Ψu���	!M��>�3
Q{�P�u�&�u�{���ZoG"A�
��s�d����~���v�o�6F�m��F�F<����_�REO\�k����k�������E�ie;�Eq���S(	�0�Yj׌EI����WfR�4�(���􏓄Rl���٨r�9�x�fc�C��<YDP�ז�fL�5�V�du��P�,$��ޚ��1�}�sݖdf!�{c�����:�z��iP�H��zZ���f��h�9:�dY�%ǱG�R{[m�8�S@�zcTU�=�3B���t������a�XgH�}��\�e1�CJbz,��^�[%���M�����p�7۪K�����{%j�pJ�����j�NŇ����Z���4@�>���n���jgF<&�g�;�@[�j�62B�^���᭷^�����\�q�0���ȝ|��)Q�c^o�a���Y��ԌH	��	���!�}��)(Uu�N�����X�>$�IÃ��|B�UTrmm��	�R����%�	2�W6�sE��z7� �sA&��w��5e���?gb��m����"�O2G=����	�(�4�x���6�yz�����rS����1�4�]�*��S&"��N�gU������)@��H��	��
<������Tj�UhZ�UǍ-�ξ�}������[�P5D��w("(x�Ec���2�+��6eu�OK�q�c�0�omY�ņ\!���.�[� �;�����|*���ZCj��[(���X�Md��9�cgD�7NN�V��cx@�$Bh�a�{��jZB���3:���C	����l�u5x��h�Q�@��/���y[�l�s�hu�r�'�����0D����F�,!�!d=_(�$�Qp�C�w���'k�hj !��y<�=��X�/s�����fk]�o�ʗɓ$��^pB�s�s�R5��94�/��(#�?�8Z�ar| ,��vr�p�(����Tr(!:�ab\*��	�
`N�j�G!���H2��;rYI��#�����^_���C�6���66��8�r��Xr�aZ��f�M� ���[�<l��3��_^&-'�M,�G�f/O�
�b���R�n�h���ۉ-�܉�G(���C���@�0����/�ݰ[��C�21 �a�o�8�{�a|��+5A��	7��?�������S3(3�xg�/L�_]"�����M�����	���ꏗ�7��� �u<�/���� ���CyF"ǘ��A���t��5���,�3�ǸOԊ��~�{H���_Bp��o���p�ńG��H��'ݑ��&�Vt��ߍW�@�·�7��	��o�u�M��7� �ޱ:N�U� ,��� ���իW�P|~�         �  x�u�Ks�6��׿��t�	`�خ�z�x��3��2a	#�P)2���{U���n<��>�=�R�R���ԭ�!N�(WU�c�RMjE��_�����6ϱoӌ��������S�J�F�����0v	P1���@K؈��q˂A��8�a�gʨZ����i-J�]㾴gXj��R;��ݒ�-�EY������� M#��ݤ#�a<c����O'VƱ�1���1��i�V��W���B�h#׻��,/�FP�}����DXA�+���-娵� ��������=k�C�?������t��Oԋ��+w����?��D������ct�)恊5��8�:S��_ɨ�C� ��ͦ;'rFpF��ߦu��q�@�m�[2j����Z�Z��Jy~E^���C�Ԟ
�Z���ԗ~N��葉�*o�4ΐ��h}������V ����N�;B��ԍ��~7�$�?_�x��ػ<ni���@���嗸��	�7�6�g��Cw�A#?��H?x����H�4tw���E>n˞����c���5�]z�0q�]��~($�|o>���#mƛ��~��3w�|J�K�1�Õ�K��Z�Ԕ
�[@��aj��`�gڈÓ,��Oϛ�,�����Ԉ�XV��ֈ�>=��2 L�\!U!�}�>��*%Vɇ�aϑ��93ߛǓ
����=Q��-7�0vG2��c��1�`�}���ty�����gb�#6���v��5o7��+y(���q$�LUZ�����q�[t��	��rZ�~��c��Q���)����з��>n��2��$p�6��D��X�p�MA��_��*Y�8'B�*�@����-T���
S�N_�m�OFP��Y2�d(?��x���#;4e���m\O\�A�� 7)� ks�Ai��]���w�;t�%��������� J���     