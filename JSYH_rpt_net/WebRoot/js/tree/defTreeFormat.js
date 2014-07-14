var DEF_TREE_FORMAT =
[
//0. left position
	0,
//1. top position
	300,
//2. show +/- buttons
	true,
//3. couple of button images (collapsed/expanded/blank)
	["/rpt_net/image/tree/c.gif", "/rpt_net/image/tree/e.gif", "/rpt_net/image/tree/b.gif"],
//4. size of images (width, height,ident for nodes w/o children)
	[16,16,16],
//5. show folder image
	true,
//6. folder images (closed/opened/document)
	["/rpt_net/image/tree/fc.gif", "/rpt_net/image/tree/fe.gif", "/rpt_net/image/tree/d.gif"],
//7. size of images (width, height)
	[16,16],
//8. identation for each level [0/*first level*/, 16/*second*/, 32/*third*/,...]
	[0,16,32,48,64,80,96,112,124],
//9. tree background color ("" - transparent)
	"",
//10. default style for all nodes
	"clsNode",
//11. styles for each level of menu (default style will be used for undefined levels)
	[],//["clsNodeL0","clsNodeL1","clsNodeL2","clsNodeL3","clsNodeL4"],
//12. true if only one branch can be opened at same time
	false,
//13. item pagging and spacing
	[0,0],
/************** PRO EXTENSIONS ********************/
//14. draw explorer like tree ( identation will be ignored )
	false,
//15. Set of explorer images (folder, openfolder, page, minus, minusbottom, plus, plusbottom, line, join, joinbottom)
	[],
//16. Explorer images width/height
	[19,16],
//17. if true state will be saved in cookies
	false,
//18. if true - relative position will be used. (tree will be opened in place where init() was called)
	false,
//19. width and height of initial rectangle for relative positioning
	[180,150],
//20. resize background //works only under IE4+, NS6+ for relatiive positioning
	false,
//21. support bgcolor changing for selected node
	false,
//22. background color for non-selected and selected node
	["white","#EEEEEE", "clsNodeSelected"]
];

