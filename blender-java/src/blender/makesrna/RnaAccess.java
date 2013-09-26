/**
 * $Id: RnaAccess.java,v 1.2 2009/09/18 05:20:56 jladere Exp $
 *
 * ***** BEGIN GPL LICENSE BLOCK *****
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software Foundation,
 * Inc., 59 Temple Place - Suite 330, Boston, MA  02111-1307, USA.
 *
 * Contributor(s): Blender Foundation (2008).
 *
 * ***** END GPL LICENSE BLOCK *****
 */
package blender.makesrna;

import java.util.Arrays;

import org.python.core.PyType;

import blender.blenkernel.IdProp;
import blender.blenkernel.Main;
import blender.blenkernel.UtilDefines;
import blender.blenkernel.bContext;
import blender.blenkernel.IdProp.IDPropertyTemplate;
import blender.blenlib.ListBaseUtil;
import blender.blenlib.StringUtil;
import blender.editors.uinterface.Resources.BIFIconID;
import blender.makesdna.DNA_ID;
import blender.makesdna.sdna.ID;
import blender.makesdna.sdna.IDProperty;
import blender.makesdna.sdna.Link;
import blender.makesdna.sdna.ListBase;
import blender.makesrna.RNATypes.CollectionPropertyIterator;
import blender.makesrna.RNATypes.EnumPropertyItem;
import blender.makesrna.RNATypes.ParameterIterator;
import blender.makesrna.RNATypes.ParameterList;
import blender.makesrna.RNATypes.PointerRNA;
import blender.makesrna.RNATypes.StructRegisterFunc;
import blender.makesrna.RNATypes.PointerRNA.IDPointer;
import blender.makesrna.rna_internal_types.ArrayIterator;
import blender.makesrna.rna_internal_types.BlenderRNA;
import blender.makesrna.rna_internal_types.BooleanPropertyRNA;
import blender.makesrna.rna_internal_types.CollectionPropertyRNA;
import blender.makesrna.rna_internal_types.EnumPropertyRNA;
import blender.makesrna.rna_internal_types.FloatPropertyRNA;
import blender.makesrna.rna_internal_types.FunctionRNA;
import blender.makesrna.rna_internal_types.IntPropertyRNA;
import blender.makesrna.rna_internal_types.IteratorSkipFunc;
import blender.makesrna.rna_internal_types.PointerPropertyRNA;
import blender.makesrna.rna_internal_types.PropCollectionEndFunc;
import blender.makesrna.rna_internal_types.PropCollectionGetFunc;
import blender.makesrna.rna_internal_types.PropCollectionNextFunc;
import blender.makesrna.rna_internal_types.PropertyRNA;
import blender.makesrna.rna_internal_types.StringPropertyRNA;
import blender.makesrna.rna_internal_types.StructRNA;
import blender.makesrna.srna.RnaBlendData;
import blender.makesrna.srna.RnaIDProperty;

public class RnaAccess {
	
	/* Types */

	public static BlenderRNA BLENDER_RNA = new BlenderRNA();

	public static StructRNA RNA_Action = new StructRNA();
	public static StructRNA RNA_ActionConstraint = new StructRNA();
	public static StructRNA RNA_ActionGroup = new StructRNA();
	public static StructRNA RNA_Actuator = new StructRNA();
	public static StructRNA RNA_ActuatorSensor = new StructRNA();
	public static StructRNA RNA_Addon = new StructRNA();
	public static StructRNA RNA_AlwaysSensor = new StructRNA();
	public static StructRNA RNA_AndController = new StructRNA();
	public static StructRNA RNA_AnimData = new StructRNA();
	public static StructRNA RNA_AnimViz = new StructRNA();
	public static StructRNA RNA_AnimVizMotionPaths = new StructRNA();
	public static StructRNA RNA_AnimVizOnionSkinning = new StructRNA();
	public static StructRNA RNA_AnyType = new StructRNA();
	public static StructRNA RNA_Area = new StructRNA("Area");
	public static StructRNA RNA_AreaLamp = new StructRNA();
	public static StructRNA RNA_Armature = new StructRNA();
	public static StructRNA RNA_ArmatureModifier = new StructRNA();
	public static StructRNA RNA_ArmatureSensor = new StructRNA();
	public static StructRNA RNA_ArrayModifier = new StructRNA();
	public static StructRNA RNA_BackgroundImage = new StructRNA();
	public static StructRNA RNA_BevelModifier = new StructRNA();
	public static StructRNA RNA_BezierSplinePoint = new StructRNA();
	public static StructRNA RNA_BlenderRNA = new StructRNA("BlenderRNA");
	public static StructRNA RNA_BlendTexture = new StructRNA();
	public static StructRNA RNA_BoidRule = new StructRNA();
	public static StructRNA RNA_BoidRuleAverageSpeed = new StructRNA();
	public static StructRNA RNA_BoidRuleAvoid = new StructRNA();
	public static StructRNA RNA_BoidRuleAvoidCollision = new StructRNA();
	public static StructRNA RNA_BoidRuleFight = new StructRNA();
	public static StructRNA RNA_BoidRuleFollowLeader = new StructRNA();
	public static StructRNA RNA_BoidRuleGoal = new StructRNA();
	public static StructRNA RNA_BoidSettings = new StructRNA();
	public static StructRNA RNA_BoidState = new StructRNA();
	public static StructRNA RNA_Bone = new StructRNA();
	public static StructRNA RNA_BoneGroup = new StructRNA();
	public static StructRNA RNA_BooleanModifier = new StructRNA();
	public static StructRNA RNA_BooleanProperty = new StructRNA();
	public static StructRNA RNA_Brush = new StructRNA();
	public static StructRNA RNA_BrushTextureSlot = new StructRNA();
	public static StructRNA RNA_BuildModifier = new StructRNA();
	public static StructRNA RNA_Camera = new StructRNA("Camera");
	public static StructRNA RNA_CastModifier = new StructRNA();
	public static StructRNA RNA_ChildOfConstraint = new StructRNA();
	public static StructRNA RNA_ChildParticle = new StructRNA();
	public static StructRNA RNA_ClampToConstraint = new StructRNA();
	public static StructRNA RNA_ClothCollisionSettings = new StructRNA();
	public static StructRNA RNA_ClothModifier = new StructRNA();
	public static StructRNA RNA_ClothSettings = new StructRNA();
	public static StructRNA RNA_CloudsTexture = new StructRNA();
	public static StructRNA RNA_CollectionProperty = new StructRNA();
	public static StructRNA RNA_CollisionModifier = new StructRNA();
	public static StructRNA RNA_CollisionSensor = new StructRNA();
	public static StructRNA RNA_CollisionSettings = new StructRNA();
	public static StructRNA RNA_ColorRamp = new StructRNA();
	public static StructRNA RNA_ColorRampElement = new StructRNA();
	public static StructRNA RNA_ColorSequence = new StructRNA();
	public static StructRNA RNA_CompositorNode = new StructRNA();
	public static StructRNA RNA_CompositorNodeAlphaOver = new StructRNA();
	public static StructRNA RNA_CompositorNodeBilateralblur = new StructRNA();
	public static StructRNA RNA_CompositorNodeBlur = new StructRNA();
	public static StructRNA RNA_CompositorNodeBrightContrast = new StructRNA();
	public static StructRNA RNA_CompositorNodeChannelMatte = new StructRNA();
	public static StructRNA RNA_CompositorNodeChromaMatte = new StructRNA();
	public static StructRNA RNA_CompositorNodeColorMatte = new StructRNA();
	public static StructRNA RNA_CompositorNodeColorSpill = new StructRNA();
	public static StructRNA RNA_CompositorNodeCombHSVA = new StructRNA();
	public static StructRNA RNA_CompositorNodeCombRGBA = new StructRNA();
	public static StructRNA RNA_CompositorNodeCombYCCA = new StructRNA();
	public static StructRNA RNA_CompositorNodeCombYUVA = new StructRNA();
	public static StructRNA RNA_CompositorNodeComposite = new StructRNA();
	public static StructRNA RNA_CompositorNodeCrop = new StructRNA();
	public static StructRNA RNA_CompositorNodeCurveRGB = new StructRNA();
	public static StructRNA RNA_CompositorNodeCurveVec = new StructRNA();
	public static StructRNA RNA_CompositorNodeDBlur = new StructRNA();
	public static StructRNA RNA_CompositorNodeDefocus = new StructRNA();
	public static StructRNA RNA_CompositorNodeDiffMatte = new StructRNA();
	public static StructRNA RNA_CompositorNodeDilateErode = new StructRNA();
	public static StructRNA RNA_CompositorNodeDisplace = new StructRNA();
	public static StructRNA RNA_CompositorNodeDistanceMatte = new StructRNA();
	public static StructRNA RNA_CompositorNodeFilter = new StructRNA();
	public static StructRNA RNA_CompositorNodeFlip = new StructRNA();
	public static StructRNA RNA_CompositorNodeGamma = new StructRNA();
	public static StructRNA RNA_CompositorNodeGlare = new StructRNA();
	public static StructRNA RNA_CompositorNodeHueSat = new StructRNA();
	public static StructRNA RNA_CompositorNodeIDMask = new StructRNA();
	public static StructRNA RNA_CompositorNodeImage = new StructRNA();
	public static StructRNA RNA_CompositorNodeInvert = new StructRNA();
	public static StructRNA RNA_CompositorNodeLensdist = new StructRNA();
	public static StructRNA RNA_CompositorNodeLevels = new StructRNA();
	public static StructRNA RNA_CompositorNodeLumaMatte = new StructRNA();
	public static StructRNA RNA_CompositorNodeMapUV = new StructRNA();
	public static StructRNA RNA_CompositorNodeMapValue = new StructRNA();
	public static StructRNA RNA_CompositorNodeMath = new StructRNA();
	public static StructRNA RNA_CompositorNodeMixRGB = new StructRNA();
	public static StructRNA RNA_CompositorNodeNormal = new StructRNA();
	public static StructRNA RNA_CompositorNodeNormalize = new StructRNA();
	public static StructRNA RNA_CompositorNodeOutputFile = new StructRNA();
	public static StructRNA RNA_CompositorNodePremulKey = new StructRNA();
	public static StructRNA RNA_CompositorNodeRGB = new StructRNA();
	public static StructRNA RNA_CompositorNodeRGBToBW = new StructRNA();
	public static StructRNA RNA_CompositorNodeRLayers = new StructRNA();
	public static StructRNA RNA_CompositorNodeRotate = new StructRNA();
	public static StructRNA RNA_CompositorNodeScale = new StructRNA();
	public static StructRNA RNA_CompositorNodeSepHSVA = new StructRNA();
	public static StructRNA RNA_CompositorNodeSepRGBA = new StructRNA();
	public static StructRNA RNA_CompositorNodeSepYCCA = new StructRNA();
	public static StructRNA RNA_CompositorNodeSepYUVA = new StructRNA();
	public static StructRNA RNA_CompositorNodeSetAlpha = new StructRNA();
	public static StructRNA RNA_CompositorNodeSplitViewer = new StructRNA();
	public static StructRNA RNA_CompositorNodeTexture = new StructRNA();
	public static StructRNA RNA_CompositorNodeTime = new StructRNA();
	public static StructRNA RNA_CompositorNodeTonemap = new StructRNA();
	public static StructRNA RNA_CompositorNodeTranslate = new StructRNA();
	public static StructRNA RNA_CompositorNodeTree = new StructRNA();
	public static StructRNA RNA_CompositorNodeValToRGB = new StructRNA();
	public static StructRNA RNA_CompositorNodeValue = new StructRNA();
	public static StructRNA RNA_CompositorNodeVecBlur = new StructRNA();
	public static StructRNA RNA_CompositorNodeViewer = new StructRNA();
	public static StructRNA RNA_CompositorNodeZcombine = new StructRNA();
	public static StructRNA RNA_ConsoleLine = new StructRNA();
	public static StructRNA RNA_Constraint = new StructRNA();
	public static StructRNA RNA_ConstraintTarget = new StructRNA();
//	public static StructRNA RNA_Context = new StructRNA("Context");
	public static StructRNA RNA_ControlFluidSettings = new StructRNA();
	public static StructRNA RNA_Controller = new StructRNA();
	public static StructRNA RNA_CopyLocationConstraint = new StructRNA();
	public static StructRNA RNA_CopyRotationConstraint = new StructRNA();
	public static StructRNA RNA_CopyScaleConstraint = new StructRNA();
	public static StructRNA RNA_CopyTransformsConstraint = new StructRNA();
	public static StructRNA RNA_Curve = new StructRNA();
	public static StructRNA RNA_CurveMap = new StructRNA();
	public static StructRNA RNA_CurveMapping = new StructRNA();
	public static StructRNA RNA_CurveMapPoint = new StructRNA();
	public static StructRNA RNA_CurveModifier = new StructRNA();
	public static StructRNA RNA_CurvePoint = new StructRNA();
	public static StructRNA RNA_DampedTrackConstraint = new StructRNA();
	public static StructRNA RNA_DecimateModifier = new StructRNA();
	public static StructRNA RNA_DelaySensor = new StructRNA();
	public static StructRNA RNA_DisplaceModifier = new StructRNA();
	public static StructRNA RNA_DistortedNoiseTexture = new StructRNA();
	public static StructRNA RNA_DomainFluidSettings = new StructRNA();
	public static StructRNA RNA_Driver = new StructRNA();
	public static StructRNA RNA_DriverTarget = new StructRNA();
	public static StructRNA RNA_DriverVariable = new StructRNA();
	public static StructRNA RNA_DupliObject = new StructRNA();
	public static StructRNA RNA_EdgeSplitModifier = new StructRNA();
	public static StructRNA RNA_EditBone = new StructRNA();
	public static StructRNA RNA_EffectorWeights = new StructRNA();
	public static StructRNA RNA_EffectSequence = new StructRNA();
	public static StructRNA RNA_EnumProperty = new StructRNA();
	public static StructRNA RNA_EnumPropertyItem = new StructRNA();
	public static StructRNA RNA_EnvironmentMap = new StructRNA();
	public static StructRNA RNA_EnvironmentMapTexture = new StructRNA();
	public static StructRNA RNA_Event = new StructRNA();
	public static StructRNA RNA_ExplodeModifier = new StructRNA();
	public static StructRNA RNA_ExpressionController = new StructRNA();
	public static StructRNA RNA_FCurve = new StructRNA();
	public static StructRNA RNA_FCurveSample = new StructRNA();
	public static StructRNA RNA_FieldSettings = new StructRNA();
	public static StructRNA RNA_FileSelectParams = new StructRNA();
	public static StructRNA RNA_FloatProperty = new StructRNA();
	public static StructRNA RNA_FloorConstraint = new StructRNA();
	public static StructRNA RNA_FluidFluidSettings = new StructRNA();
	public static StructRNA RNA_FluidSettings = new StructRNA();
	public static StructRNA RNA_FluidSimulationModifier = new StructRNA();
	public static StructRNA RNA_FModifier = new StructRNA();
	public static StructRNA RNA_FModifierCycles = new StructRNA();
	public static StructRNA RNA_FModifierEnvelope = new StructRNA();
	public static StructRNA RNA_FModifierEnvelopeControlPoint = new StructRNA();
	public static StructRNA RNA_FModifierFunctionGenerator = new StructRNA();
	public static StructRNA RNA_FModifierGenerator = new StructRNA();
	public static StructRNA RNA_FModifierLimits = new StructRNA();
	public static StructRNA RNA_FModifierNoise = new StructRNA();
	public static StructRNA RNA_FModifierPython = new StructRNA();
	public static StructRNA RNA_FModifierStepped = new StructRNA();
	public static StructRNA RNA_FollowPathConstraint = new StructRNA();
	public static StructRNA RNA_Function = new StructRNA();
	public static StructRNA RNA_GameBooleanProperty = new StructRNA();
	public static StructRNA RNA_GameFloatProperty = new StructRNA();
	public static StructRNA RNA_GameIntProperty = new StructRNA();
	public static StructRNA RNA_GameObjectSettings = new StructRNA();
	public static StructRNA RNA_GameProperty = new StructRNA();
	public static StructRNA RNA_GameSoftBodySettings = new StructRNA();
	public static StructRNA RNA_GameStringProperty = new StructRNA();
	public static StructRNA RNA_GameTimerProperty = new StructRNA();
	public static StructRNA RNA_GlowSequence = new StructRNA();
	public static StructRNA RNA_GPencilFrame = new StructRNA();
	public static StructRNA RNA_GPencilLayer = new StructRNA();
	public static StructRNA RNA_GPencilStroke = new StructRNA();
	public static StructRNA RNA_GPencilStrokePoint = new StructRNA();
	public static StructRNA RNA_GreasePencil = new StructRNA();
	public static StructRNA RNA_Group = new StructRNA();
//	public static StructRNA RNA_Header = new StructRNA("Header");
	public static StructRNA RNA_HemiLamp = new StructRNA();
	public static StructRNA RNA_Histogram = new StructRNA();
	public static StructRNA RNA_HookModifier = new StructRNA();
	public static StructRNA RNA_ID = new StructRNA("ID");
	public static StructRNA RNA_IDProperty = new StructRNA("IDProperty");
	public static StructRNA RNA_IDPropertyGroup = new StructRNA();
	public static StructRNA RNA_IKParam = new StructRNA();
	public static StructRNA RNA_Image = new StructRNA();
	public static StructRNA RNA_ImagePaint = new StructRNA();
	public static StructRNA RNA_ImageSequence = new StructRNA();
	public static StructRNA RNA_ImageTexture = new StructRNA();
	public static StructRNA RNA_ImageUser = new StructRNA();
	public static StructRNA RNA_InflowFluidSettings = new StructRNA();
	public static StructRNA RNA_IntProperty = new StructRNA();
	public static StructRNA RNA_Itasc = new StructRNA();
	public static StructRNA RNA_JoystickSensor = new StructRNA();
	public static StructRNA RNA_Key = new StructRNA();
	public static StructRNA RNA_KeyboardSensor = new StructRNA();
	public static StructRNA RNA_KeyConfig = new StructRNA();
	public static StructRNA RNA_Keyframe = new StructRNA();
	public static StructRNA RNA_KeyingSet = new StructRNA();
	public static StructRNA RNA_KeyingSetPath = new StructRNA();
	public static StructRNA RNA_KeyingSetInfo = new StructRNA();
	public static StructRNA RNA_KeyMap = new StructRNA();
	public static StructRNA RNA_KeyMapItem = new StructRNA();
	public static StructRNA RNA_KinematicConstraint = new StructRNA();
	public static StructRNA RNA_Lamp = new StructRNA("Lamp");
	public static StructRNA RNA_LampSkySettings = new StructRNA();
	public static StructRNA RNA_LampTextureSlot = new StructRNA();
	public static StructRNA RNA_Lattice = new StructRNA();
	public static StructRNA RNA_LatticeModifier = new StructRNA();
	public static StructRNA RNA_LatticePoint = new StructRNA();
	public static StructRNA RNA_Library = new StructRNA("Library");
	public static StructRNA RNA_LimitDistanceConstraint = new StructRNA();
	public static StructRNA RNA_LimitLocationConstraint = new StructRNA();
	public static StructRNA RNA_LimitRotationConstraint = new StructRNA();
	public static StructRNA RNA_LimitScaleConstraint = new StructRNA();
	public static StructRNA RNA_LockedTrackConstraint = new StructRNA();
	public static StructRNA RNA_Macro = new StructRNA();
	public static StructRNA RNA_MagicTexture = new StructRNA();
//	public static StructRNA RNA_BlendData = new StructRNA("BlendData");
	public static StructRNA RNA_MarbleTexture = new StructRNA();
	public static StructRNA RNA_MaskModifier = new StructRNA();
	public static StructRNA RNA_Material = new StructRNA();
	public static StructRNA RNA_MaterialHalo = new StructRNA();
	public static StructRNA RNA_MaterialPhysics = new StructRNA();
	public static StructRNA RNA_MaterialRaytraceMirror = new StructRNA();
	public static StructRNA RNA_MaterialRaytraceTransparency = new StructRNA();
	public static StructRNA RNA_MaterialSlot = new StructRNA();
	public static StructRNA RNA_MaterialStrand = new StructRNA();
	public static StructRNA RNA_MaterialSubsurfaceScattering = new StructRNA();
	public static StructRNA RNA_MaterialTextureSlot = new StructRNA();
	public static StructRNA RNA_MaterialVolume = new StructRNA();
//	public static StructRNA RNA_Menu = new StructRNA("Menu");
	public static StructRNA RNA_Mesh = new StructRNA();
	public static StructRNA RNA_MeshColor = new StructRNA();
	public static StructRNA RNA_MeshColorLayer = new StructRNA();
	public static StructRNA RNA_MeshDeformModifier = new StructRNA();
	public static StructRNA RNA_MeshEdge = new StructRNA();
	public static StructRNA RNA_MeshFace = new StructRNA();
	public static StructRNA RNA_MeshFloatProperty = new StructRNA();
	public static StructRNA RNA_MeshFloatPropertyLayer = new StructRNA();
	public static StructRNA RNA_MeshIntProperty = new StructRNA();
	public static StructRNA RNA_MeshIntPropertyLayer = new StructRNA();
	public static StructRNA RNA_MeshSticky = new StructRNA();
	public static StructRNA RNA_MeshStringProperty = new StructRNA();
	public static StructRNA RNA_MeshStringPropertyLayer = new StructRNA();
	public static StructRNA RNA_MeshTextureFace = new StructRNA();
	public static StructRNA RNA_MeshTextureFaceLayer = new StructRNA();
	public static StructRNA RNA_MeshVertex = new StructRNA();
	public static StructRNA RNA_MessageSensor = new StructRNA();
	public static StructRNA RNA_MetaBall = new StructRNA();
	public static StructRNA RNA_MetaElement = new StructRNA();
	public static StructRNA RNA_MetaSequence = new StructRNA();
	public static StructRNA RNA_MirrorModifier = new StructRNA();
	public static StructRNA RNA_Modifier = new StructRNA();
	public static StructRNA RNA_MotionPath = new StructRNA();
	public static StructRNA RNA_MotionPathVert = new StructRNA();
	public static StructRNA RNA_MouseSensor = new StructRNA();
	public static StructRNA RNA_MovieSequence = new StructRNA();
	public static StructRNA RNA_MulticamSequence = new StructRNA();
	public static StructRNA RNA_MultiresModifier = new StructRNA();
	public static StructRNA RNA_MusgraveTexture = new StructRNA();
	public static StructRNA RNA_NandController = new StructRNA();
	public static StructRNA RNA_NearSensor = new StructRNA();
	public static StructRNA RNA_NlaStrip = new StructRNA();
	public static StructRNA RNA_NlaTrack = new StructRNA();
	public static StructRNA RNA_Node = new StructRNA();
	public static StructRNA RNA_NodeGroup = new StructRNA();
	public static StructRNA RNA_NodeLink = new StructRNA();
	public static StructRNA RNA_NodeSocket = new StructRNA();
	public static StructRNA RNA_NodeTree = new StructRNA();
	public static StructRNA RNA_NoiseTexture = new StructRNA();
	public static StructRNA RNA_NorController = new StructRNA();
	public static StructRNA RNA_Object = new StructRNA("Object");
	public static StructRNA RNA_ObjectBase = new StructRNA();
	public static StructRNA RNA_ObstacleFluidSettings = new StructRNA();
//	public static StructRNA RNA_Operator = new StructRNA("Operator");
	public static StructRNA RNA_OperatorFileListElement = new StructRNA();
	public static StructRNA RNA_OperatorMousePath = new StructRNA();
//	public static StructRNA RNA_OperatorProperties = new StructRNA("OperatorProperties");
	public static StructRNA RNA_OperatorStrokeElement = new StructRNA();
	public static StructRNA RNA_OperatorTypeMacro = new StructRNA();
	public static StructRNA RNA_OrController = new StructRNA();
	public static StructRNA RNA_OutflowFluidSettings = new StructRNA();
	public static StructRNA RNA_PackedFile = new StructRNA();
	public static StructRNA RNA_Paint = new StructRNA();
	public static StructRNA RNA_Panel = new StructRNA("Panel");
	public static StructRNA RNA_Particle = new StructRNA();
	public static StructRNA RNA_ParticleBrush = new StructRNA();
	public static StructRNA RNA_ParticleDupliWeight = new StructRNA();
	public static StructRNA RNA_ParticleEdit = new StructRNA();
	public static StructRNA RNA_ParticleFluidSettings = new StructRNA();
	public static StructRNA RNA_ParticleHairKey = new StructRNA();
	public static StructRNA RNA_ParticleInstanceModifier = new StructRNA();
	public static StructRNA RNA_ParticleKey = new StructRNA();
	public static StructRNA RNA_ParticleSettings = new StructRNA();
	public static StructRNA RNA_SPHFluidSettings = new StructRNA();
	public static StructRNA RNA_ParticleSystem = new StructRNA();
	public static StructRNA RNA_ParticleSystemModifier = new StructRNA();
	public static StructRNA RNA_ParticleTarget = new StructRNA();
	public static StructRNA RNA_PivotConstraint = new StructRNA();
	public static StructRNA RNA_PluginSequence = new StructRNA();
	public static StructRNA RNA_PluginTexture = new StructRNA();
	public static StructRNA RNA_PointCache = new StructRNA();
	public static StructRNA RNA_PointDensity = new StructRNA();
	public static StructRNA RNA_PointDensityTexture = new StructRNA();
	public static StructRNA RNA_PointerProperty = new StructRNA();
	public static StructRNA RNA_PointLamp = new StructRNA();
	public static StructRNA RNA_Pose = new StructRNA();
	public static StructRNA RNA_PoseBone = new StructRNA();
//	public static StructRNA RNA_Property = new StructRNA();
	public static StructRNA RNA_PropertySensor = new StructRNA();
	public static StructRNA RNA_PythonConstraint = new StructRNA();
	public static StructRNA RNA_PythonController = new StructRNA();
	public static StructRNA RNA_RadarSensor = new StructRNA();
	public static StructRNA RNA_RandomSensor = new StructRNA();
	public static StructRNA RNA_RaySensor = new StructRNA();
	public static StructRNA RNA_Region = new StructRNA("Region");
	public static StructRNA RNA_RenderEngine = new StructRNA();
	public static StructRNA RNA_RenderLayer = new StructRNA();
	public static StructRNA RNA_RenderPass = new StructRNA();
	public static StructRNA RNA_RenderResult = new StructRNA();
	public static StructRNA RNA_RenderSettings = new StructRNA();
	public static StructRNA RNA_RGBANodeSocket = new StructRNA();
	public static StructRNA RNA_RigidBodyJointConstraint = new StructRNA();
//	public static StructRNA RNA_Scene = new StructRNA("Scene");
	public static StructRNA RNA_SceneGameData = new StructRNA();
	public static StructRNA RNA_SceneRenderLayer = new StructRNA();
	public static StructRNA RNA_SceneSequence = new StructRNA();
	public static StructRNA RNA_Scopes = new StructRNA();
//	public static StructRNA RNA_Screen = new StructRNA("Screen");
	public static StructRNA RNA_ScrewModifier = new StructRNA();
	public static StructRNA RNA_Sculpt = new StructRNA();
	public static StructRNA RNA_Sensor = new StructRNA();
	public static StructRNA RNA_Sequence = new StructRNA();
	public static StructRNA RNA_SequenceColorBalance = new StructRNA();
	public static StructRNA RNA_SequenceCrop = new StructRNA();
	public static StructRNA RNA_SequenceEditor = new StructRNA();
	public static StructRNA RNA_SequenceElement = new StructRNA();
	public static StructRNA RNA_SequenceProxy = new StructRNA();
	public static StructRNA RNA_SequenceTransform = new StructRNA();
	public static StructRNA RNA_ShaderNode = new StructRNA();
	public static StructRNA RNA_ShaderNodeCameraData = new StructRNA();
	public static StructRNA RNA_ShaderNodeCombineRGB = new StructRNA();
	public static StructRNA RNA_ShaderNodeExtendedMaterial = new StructRNA();
	public static StructRNA RNA_ShaderNodeGeometry = new StructRNA();
	public static StructRNA RNA_ShaderNodeHueSaturation = new StructRNA();
	public static StructRNA RNA_ShaderNodeInvert = new StructRNA();
	public static StructRNA RNA_ShaderNodeMapping = new StructRNA();
	public static StructRNA RNA_ShaderNodeMaterial = new StructRNA();
	public static StructRNA RNA_ShaderNodeMath = new StructRNA();
	public static StructRNA RNA_ShaderNodeMixRGB = new StructRNA();
	public static StructRNA RNA_ShaderNodeNormal = new StructRNA();
	public static StructRNA RNA_ShaderNodeOutput = new StructRNA();
	public static StructRNA RNA_ShaderNodeRGB = new StructRNA();
	public static StructRNA RNA_ShaderNodeRGBCurve = new StructRNA();
	public static StructRNA RNA_ShaderNodeRGBToBW = new StructRNA();
	public static StructRNA RNA_ShaderNodeSeparateRGB = new StructRNA();
	public static StructRNA RNA_ShaderNodeSqueeze = new StructRNA();
	public static StructRNA RNA_ShaderNodeTexture = new StructRNA();
	public static StructRNA RNA_ShaderNodeTree = new StructRNA();
	public static StructRNA RNA_ShaderNodeValToRGB = new StructRNA();
	public static StructRNA RNA_ShaderNodeValue = new StructRNA();
	public static StructRNA RNA_ShaderNodeVectorCurve = new StructRNA();
	public static StructRNA RNA_ShaderNodeVectorMath = new StructRNA();
	public static StructRNA RNA_ShapeKey = new StructRNA();
	public static StructRNA RNA_ShapeKeyBezierPoint = new StructRNA();
	public static StructRNA RNA_ShapeKeyCurvePoint = new StructRNA();
	public static StructRNA RNA_ShapeKeyPoint = new StructRNA();
	public static StructRNA RNA_ShrinkwrapConstraint = new StructRNA();
	public static StructRNA RNA_ShrinkwrapModifier = new StructRNA();
	public static StructRNA RNA_SimpleDeformModifier = new StructRNA();
	public static StructRNA RNA_SmokeCollSettings = new StructRNA();
	public static StructRNA RNA_SmokeDomainSettings = new StructRNA();
	public static StructRNA RNA_SmokeFlowSettings = new StructRNA();
	public static StructRNA RNA_SmokeModifier = new StructRNA();
	public static StructRNA RNA_SmoothModifier = new StructRNA();
	public static StructRNA RNA_SoftBodyModifier = new StructRNA();
	public static StructRNA RNA_SoftBodySettings = new StructRNA();
	public static StructRNA RNA_SolidifyModifier = new StructRNA();
	public static StructRNA RNA_Sound = new StructRNA();
	public static StructRNA RNA_SoundSequence = new StructRNA();
//	public static StructRNA RNA_Space = new StructRNA("Space");
//	public static StructRNA RNA_SpaceView3D = new StructRNA("SpaceView3D");
	public static StructRNA RNA_SpaceConsole = new StructRNA();
	public static StructRNA RNA_SpaceDopeSheetEditor = new StructRNA();
	public static StructRNA RNA_SpaceFileBrowser = new StructRNA();
	public static StructRNA RNA_SpaceGraphEditor = new StructRNA();
	public static StructRNA RNA_SpaceImageEditor = new StructRNA();
//	public static StructRNA RNA_SpaceInfo = new StructRNA("SpaceInfo");
	public static StructRNA RNA_SpaceLogicEditor = new StructRNA();
	public static StructRNA RNA_SpaceNLA = new StructRNA();
//	public static StructRNA RNA_SpaceNodeEditor = new StructRNA();
//	public static StructRNA RNA_SpaceOutliner = new StructRNA("SpaceOutliner");
	public static StructRNA RNA_SpaceProperties = new StructRNA();
	public static StructRNA RNA_SpaceSequenceEditor = new StructRNA();
	public static StructRNA RNA_SpaceTextEditor = new StructRNA();
//	public static StructRNA RNA_SpaceTimeline = new StructRNA("SpaceTimeline");
	public static StructRNA RNA_SpaceUserPreferences = new StructRNA("SpaceUserPreferences");
	public static StructRNA RNA_SpaceUVEditor = new StructRNA();
	public static StructRNA RNA_SpeedControlSequence = new StructRNA();
	public static StructRNA RNA_Spline = new StructRNA();
	public static StructRNA RNA_SplineIKConstraint = new StructRNA();
	public static StructRNA RNA_SpotLamp = new StructRNA();
	public static StructRNA RNA_StretchToConstraint = new StructRNA();
	public static StructRNA RNA_StringProperty = new StructRNA();
	public static StructRNA RNA_Struct = new StructRNA("Struct");
	public static StructRNA RNA_StucciTexture = new StructRNA();
	public static StructRNA RNA_SubsurfModifier = new StructRNA();
	public static StructRNA RNA_SunLamp = new StructRNA();
	public static StructRNA RNA_SurfaceCurve = new StructRNA();
	public static StructRNA RNA_SurfaceModifier = new StructRNA();
	public static StructRNA RNA_TexMapping = new StructRNA();
	public static StructRNA RNA_Text = new StructRNA();
	public static StructRNA RNA_TextBox = new StructRNA();
	public static StructRNA RNA_TextCharacterFormat = new StructRNA();
	public static StructRNA RNA_TextCurve = new StructRNA();
	public static StructRNA RNA_TextLine = new StructRNA();
	public static StructRNA RNA_TextMarker = new StructRNA();
	public static StructRNA RNA_Texture = new StructRNA();
	public static StructRNA RNA_TextureNode = new StructRNA();
	public static StructRNA RNA_TextureNodeBricks = new StructRNA();
	public static StructRNA RNA_TextureNodeChecker = new StructRNA();
	public static StructRNA RNA_TextureNodeCompose = new StructRNA();
	public static StructRNA RNA_TextureNodeCoordinates = new StructRNA();
	public static StructRNA RNA_TextureNodeCurveRGB = new StructRNA();
	public static StructRNA RNA_TextureNodeCurveTime = new StructRNA();
	public static StructRNA RNA_TextureNodeDecompose = new StructRNA();
	public static StructRNA RNA_TextureNodeDistance = new StructRNA();
	public static StructRNA RNA_TextureNodeHueSaturation = new StructRNA();
	public static StructRNA RNA_TextureNodeImage = new StructRNA();
	public static StructRNA RNA_TextureNodeInvert = new StructRNA();
	public static StructRNA RNA_TextureNodeMath = new StructRNA();
	public static StructRNA RNA_TextureNodeMixRGB = new StructRNA();
	public static StructRNA RNA_TextureNodeOutput = new StructRNA();
	public static StructRNA RNA_TextureNodeRGBToBW = new StructRNA();
	public static StructRNA RNA_TextureNodeRotate = new StructRNA();
	public static StructRNA RNA_TextureNodeScale = new StructRNA();
	public static StructRNA RNA_TextureNodeTexture = new StructRNA();
	public static StructRNA RNA_TextureNodeTranslate = new StructRNA();
	public static StructRNA RNA_TextureNodeTree = new StructRNA();
	public static StructRNA RNA_TextureNodeValToNor = new StructRNA();
	public static StructRNA RNA_TextureNodeValToRGB = new StructRNA();
	public static StructRNA RNA_TextureNodeViewer = new StructRNA();
	public static StructRNA RNA_TextureSlot = new StructRNA();
	public static StructRNA RNA_Theme = new StructRNA();
	public static StructRNA RNA_ThemeAudioWindow = new StructRNA();
	public static StructRNA RNA_ThemeBoneColorSet = new StructRNA();
	public static StructRNA RNA_ThemeConsole = new StructRNA();
	public static StructRNA RNA_ThemeDopeSheet = new StructRNA();
	public static StructRNA RNA_ThemeFileBrowser = new StructRNA();
	public static StructRNA RNA_ThemeFontStyle = new StructRNA();
	public static StructRNA RNA_ThemeGraphEditor = new StructRNA();
	public static StructRNA RNA_ThemeImageEditor = new StructRNA();
	public static StructRNA RNA_ThemeInfo = new StructRNA();
	public static StructRNA RNA_ThemeLogicEditor = new StructRNA();
	public static StructRNA RNA_ThemeNLAEditor = new StructRNA();
	public static StructRNA RNA_ThemeNodeEditor = new StructRNA();
	public static StructRNA RNA_ThemeOutliner = new StructRNA();
	public static StructRNA RNA_ThemeProperties = new StructRNA();
	public static StructRNA RNA_ThemeSequenceEditor = new StructRNA();
	public static StructRNA RNA_ThemeStyle = new StructRNA();
	public static StructRNA RNA_ThemeTextEditor = new StructRNA();
	public static StructRNA RNA_ThemeTimeline = new StructRNA();
	public static StructRNA RNA_ThemeUserInterface = new StructRNA();
	public static StructRNA RNA_ThemeUserPreferences = new StructRNA();
	public static StructRNA RNA_ThemeView3D = new StructRNA();
	public static StructRNA RNA_ThemeWidgetColors = new StructRNA();
	public static StructRNA RNA_ThemeWidgetStateColors = new StructRNA();
	public static StructRNA RNA_TimelineMarker = new StructRNA();
//	public static StructRNA RNA_ToolSettings = new StructRNA("ToolSettings");
	public static StructRNA RNA_TouchSensor = new StructRNA();
	public static StructRNA RNA_TrackToConstraint = new StructRNA();
	public static StructRNA RNA_TransformConstraint = new StructRNA();
	public static StructRNA RNA_TransformSequence = new StructRNA();
	public static StructRNA RNA_UILayout = new StructRNA("UILayout");
	public static StructRNA RNA_UIListItem = new StructRNA();
	public static StructRNA RNA_UnitSettings = new StructRNA();
	public static StructRNA RNA_UnknownType = new StructRNA();
	public static StructRNA RNA_UserPreferences = new StructRNA("UserPreferences");
	public static StructRNA RNA_UserPreferencesEdit = new StructRNA();
	public static StructRNA RNA_UserPreferencesFilePaths = new StructRNA();
	public static StructRNA RNA_UserPreferencesSystem = new StructRNA();
	public static StructRNA RNA_UserPreferencesView = new StructRNA();
	public static StructRNA RNA_UserSolidLight = new StructRNA();
	public static StructRNA RNA_UVProjectModifier = new StructRNA();
	public static StructRNA RNA_UVProjector = new StructRNA();
	public static StructRNA RNA_ValueNodeSocket = new StructRNA();
	public static StructRNA RNA_VectorFont = new StructRNA();
	public static StructRNA RNA_VectorNodeSocket = new StructRNA();
	public static StructRNA RNA_VertexGroup = new StructRNA();
	public static StructRNA RNA_VertexGroupElement = new StructRNA();
	public static StructRNA RNA_VertexPaint = new StructRNA();
	public static StructRNA RNA_VoronoiTexture = new StructRNA();
	public static StructRNA RNA_VoxelData = new StructRNA();
	public static StructRNA RNA_VoxelDataTexture = new StructRNA();
	public static StructRNA RNA_WaveModifier = new StructRNA();
//	public static StructRNA RNA_Window = new StructRNA("Window");
	public static StructRNA RNA_WindowManager = new StructRNA("WindowManager");
	public static StructRNA RNA_WipeSequence = new StructRNA();
	public static StructRNA RNA_WoodTexture = new StructRNA();
	public static StructRNA RNA_World = new StructRNA();
	public static StructRNA RNA_WorldAmbientOcclusion = new StructRNA();
	public static StructRNA RNA_WorldMistSettings = new StructRNA();
	public static StructRNA RNA_WorldStarsSettings = new StructRNA();
	public static StructRNA RNA_WorldTextureSlot = new StructRNA();
	public static StructRNA RNA_XnorController = new StructRNA();
	public static StructRNA RNA_XorController = new StructRNA();

// TMP
//public static StructRNA RNA_UnknownType = new StructRNA();

/* Init/Exit */

public static void RNA_init()
{
//	StructRNA srna;
//	PropertyRNA prop;
//
//	for(srna=BLENDER_RNA.structs.first; srna!=null; srna=(StructRNA)srna.cont.next) {
//		if(!srna.cont.prophash) {
//			srna.cont.prophash= BLI_ghash_new(BLI_ghashutil_strhash, BLI_ghashutil_strcmp);
//
//			for(prop=srna.cont.properties.first; prop; prop=prop.next)
//				if(!(prop.flag & PROP_BUILTIN))
//					BLI_ghash_insert(srna.cont.prophash, (void*)prop.identifier, prop);
//		}
//	}
}

//void RNA_exit()
//{
//	StructRNA *srna;
//
//	for(srna=BLENDER_RNA.structs.first; srna; srna=srna.cont.next) {
//		if(srna.cont.prophash) {
//			BLI_ghash_free(srna.cont.prophash, NULL, NULL);
//			srna.cont.prophash= NULL;
//		}
//	}
//
//	RNA_free(&BLENDER_RNA);
//}
//
///* Pointer */
//
//PointerRNA PointerRNA_NULL = {{0}, 0, 0};
public static final PointerRNA PointerRNA_NULL= new PointerRNA();

public static void RNA_main_pointer_create(Main main, PointerRNA r_ptr)
{
	r_ptr.id.data= null;
//	r_ptr.type= RNA_Main;
	r_ptr.type= new StructRNA();
	r_ptr.type.py_type= PyType.fromClass(RnaBlendData.class);
	r_ptr.data= main;
}

public static void RNA_id_pointer_create(ID id, PointerRNA r_ptr)
{
//	PointerRNA tmp = new PointerRNA();
	StructRNA type, idtype= null;

	if(id!=null) {
//		memset(&tmp, 0, sizeof(tmp));
//		tmp.data= id;
//		idtype= rna_ID_refine(tmp);
//
//		while(idtype.refine) {
//			type= idtype.refine(&tmp);
//
//			if(type == idtype)
//				break;
//			else
//				idtype= type;
//		}
	}

//	r_ptr.id.data= id;
	r_ptr.type= idtype;
	r_ptr.data= id;
}

public static void RNA_pointer_create(ID id, StructRNA type, Object data, PointerRNA r_ptr)
{
//	PointerRNA tmp = new PointerRNA();
//	StructRNA idtype= null;

//	if(id!=null) {
////		memset(&tmp, 0, sizeof(tmp));
//		tmp.data= id;
//		idtype= rna_ID_refine(tmp);
//	}

	r_ptr.id.data= id;
	r_ptr.type= type;
	r_ptr.data= data;

	if(data!=null) {
		while(r_ptr.type!=null && r_ptr.type.refine!=null) {
			StructRNA rtype= r_ptr.type.refine.refine(r_ptr);

			if(rtype == r_ptr.type)
				break;
			else
				r_ptr.type= rtype;
		}
	}
}

static void rna_pointer_inherit_id(StructRNA type, PointerRNA parent, PointerRNA ptr)
{
	if(type!=null && (type.flag & RNATypes.STRUCT_ID)!=0) {
		ptr.id.data= ptr.data;
	}
	else {
		ptr.id.data= parent.id.data;
	}
}

//void RNA_blender_rna_pointer_create(PointerRNA *r_ptr)
//{
//	r_ptr.id.data= NULL;
//	r_ptr.type= &RNA_BlenderRNA;
//	r_ptr.data= &BLENDER_RNA;
//}

public static PointerRNA rna_pointer_inherit_refine(PointerRNA ptr, StructRNA type, Object data)
{
	if(data!=null) {
		PointerRNA result = new PointerRNA();
		result.data= data;
		result.type= type;
//		rna_pointer_inherit_id(type, ptr, &result);

		while(result.type.refine!=null) {
			type= result.type.refine.refine(result);

			if(type == result.type)
				break;
			else
				result.type= type;
		}
		return result;
	}
	else {
		return PointerRNA_NULL;
	}
}

/* ID Properties */

/* return a UI local ID prop definition for this prop */
//IDProperty *rna_idproperty_ui(PropertyRNA *prop)
//{
//	IDProperty *idprop;
//
//	for(idprop= ((IDProperty *)prop)->prev; idprop; idprop= idprop->prev) {
//		if (strcmp(RNA_IDP_UI, idprop->name)==0)
//			break;
//	}
//
//	if(idprop==NULL) {
//		for(idprop= ((IDProperty *)prop)->next; idprop; idprop= idprop->next) {
//			if (strcmp(RNA_IDP_UI, idprop->name)==0)
//				break;
//		}
//	}
//
//	if (idprop) {
//		return IDP_GetPropertyTypeFromGroup(idprop, ((IDProperty *)prop)->name, IDP_GROUP);
//	}
//
//	return NULL;
//}

static IDProperty RNA_struct_idprops(PointerRNA ptr, boolean create)
{
	StructRNA type= ptr.type;

	if(type!=null && type.idproperties!=null)
		return type.idproperties.prop(ptr, create);

	return null;
}

public static boolean RNA_struct_idprops_check(StructRNA srna)
{
	return (srna!=null && srna.idproperties!=null);
}

static IDProperty rna_idproperty_find(PointerRNA ptr, String name)
{
	IDProperty group= RNA_struct_idprops(ptr, false);
	IDProperty idprop;

	if(group!=null) {
		for(idprop=(IDProperty)group.data.group.first; idprop!=null; idprop=idprop.next)
			if(StringUtil.strcmp(idprop.name,0, StringUtil.toCString(name),0) == 0)
				return idprop;
	}

	return null;
}

static boolean rna_idproperty_verify_valid(PropertyRNA prop, IDProperty idprop)
{
	/* this verifies if the idproperty actually matches the property
	 * description and otherwise removes it. this is to ensure that
	 * rna property access is type safe, e.g. if you defined the rna
	 * to have a certain array length you can count on that staying so */

	switch(idprop.type) {
		case DNA_ID.IDP_IDPARRAY:
			if(prop.type != RNATypes.PROP_COLLECTION)
				return false;
			break;
		case DNA_ID.IDP_ARRAY:
			if(prop.arraylength[0] != idprop.len)
				return false;

			if(idprop.subtype == DNA_ID.IDP_FLOAT && prop.type != RNATypes.PROP_FLOAT)
				return false;
			if(idprop.subtype == DNA_ID.IDP_INT && !(prop.type == RNATypes.PROP_BOOLEAN || prop.type == RNATypes.PROP_INT || prop.type == RNATypes.PROP_ENUM))
				return false;

			break;
		case DNA_ID.IDP_INT:
			if(!(prop.type == RNATypes.PROP_BOOLEAN || prop.type == RNATypes.PROP_INT || prop.type == RNATypes.PROP_ENUM))
				return false;
			break;
		case DNA_ID.IDP_FLOAT:
		case DNA_ID.IDP_DOUBLE:
			if(prop.type != RNATypes.PROP_FLOAT)
				return false;
			break;
		case DNA_ID.IDP_STRING:
			if(prop.type != RNATypes.PROP_STRING)
				return false;
			break;
		case DNA_ID.IDP_GROUP:
			if(prop.type != RNATypes.PROP_POINTER)
				return false;
			break;
		default:
			return false;
	}

	return true;
}

private static PropertyRNA typemap[] =
	{(PropertyRNA)RnaIDProperty.rna_IDProperty_string,
	 (PropertyRNA)RnaIDProperty.rna_IDProperty_int,
	 (PropertyRNA)RnaIDProperty.rna_IDProperty_float,
	 null, null, null,
	 (PropertyRNA)RnaIDProperty.rna_IDProperty_group, null,
	 (PropertyRNA)RnaIDProperty.rna_IDProperty_double};

private static PropertyRNA arraytypemap[] =
	{null, (PropertyRNA)RnaIDProperty.rna_IDProperty_int_array,
	 (PropertyRNA)RnaIDProperty.rna_IDProperty_float_array,
	 null, null, null,
	 (PropertyRNA)RnaIDProperty.rna_IDProperty_collection, null,
	 (PropertyRNA)RnaIDProperty.rna_IDProperty_double_array};

//public static IDProperty rna_idproperty_check(PropertyRNA[] prop, PointerRNA ptr)
public static IDProperty rna_idproperty_check(Object[] props, PointerRNA ptr)
{
	/* This is quite a hack, but avoids some complexity in the API. we
	 * pass IDProperty structs as PropertyRNA pointers to the outside.
	 * We store some bytes in PropertyRNA structs that allows us to
	 * distinguish it from IDProperty structs. If it is an ID property,
	 * we look up an IDP PropertyRNA based on the type, and set the data
	 * pointer to the IDProperty. */

	if(props[0] instanceof PropertyRNA) {
		PropertyRNA prop = (PropertyRNA)props[0];
//	if(prop[0].magic == rna_internal_types.RNA_MAGIC) {
		if((prop.flag & RNATypes.PROP_IDPROPERTY)!=0) {
			System.out.println("rna_idproperty_check found: "+prop.identifier);
			IDProperty idprop= rna_idproperty_find(ptr, prop.identifier);

			if(idprop!=null && !rna_idproperty_verify_valid(prop, idprop)) {
				IDProperty group= RNA_struct_idprops(ptr, false);

//				IDP_RemFromGroup(group, idprop);
//				IDP_FreeProperty(idprop);
//				MEM_freeN(idprop);
				return null;
			}

			return idprop;
		}
		else
			return null;
	}

	{
		IDProperty idprop= (IDProperty)props[0];

		if(idprop.type == DNA_ID.IDP_ARRAY)
			props[0]= arraytypemap[(int)(idprop.subtype)];
		else
			props[0]= typemap[(int)(idprop.type)];

		return idprop;
	}
}

public static PropertyRNA rna_ensure_property(PropertyRNA prop)
{
	/* the quick version if we don't need the idproperty */

	if (prop == null) {
		System.out.println("rna_ensure_property: null");
		Thread.dumpStack();
	}
	
//	if(prop.magic == rna_internal_types.RNA_MAGIC)
		return prop;
//
//	{
//		IDProperty idprop= (IDProperty)prop;
//
//		if(idprop.type == DNA_ID.IDP_ARRAY)
//			return arraytypemap[(int)(idprop.subtype)];
//		else
//			return typemap[(int)(idprop.type)];
//	}
}

//public static String rna_ensure_property_identifier(PropertyRNA prop)
//{
//	if(prop.magic == rna_internal_types.RNA_MAGIC)
//		return prop.identifier;
//	else
//		return StringUtil.toJString(((IDProperty)prop).name,0);
//}

//const char *rna_ensure_property_name(PropertyRNA *prop)
//{
//	if(prop.magic == RNA_MAGIC)
//		return prop.name;
//	else
//		return ((IDProperty*)prop).name;
//}

//public static int rna_ensure_property_array_length(PointerRNA ptr, PropertyRNA prop)
//{
//	if(prop.magic == rna_internal_types.RNA_MAGIC) {
////		int arraylen[RNA_MAX_ARRAY_DIMENSION];
////		return (prop.getlength!=null && ptr.data!=null)? prop.getlength(ptr, arraylen): prop.totarraylength;
//		return prop.totarraylength;
//	}
//	else {
//		IDProperty idprop= (IDProperty)prop;
//
//		if(idprop.type == DNA_ID.IDP_ARRAY)
//			return idprop.len;
//		else
//			return 0;
//	}
//}

/* Structs */

public static String RNA_struct_identifier(StructRNA type)
{
	return type.identifier;
}

//const char *RNA_struct_ui_name(StructRNA *type)
//{
//	return type.name;
//}

public static int RNA_struct_ui_icon(StructRNA type)
{
	if(type!=null)
		return type.icon;
	else
		return BIFIconID.ICON_DOT.ordinal();
}

//const char *RNA_struct_ui_description(StructRNA *type)
//{
//	return type.description;
//}

public static PropertyRNA RNA_struct_name_property(StructRNA type)
{
	return type.nameproperty;
}

public static PropertyRNA RNA_struct_iterator_property(StructRNA type)
{
	if (type.iteratorproperty == null) {
		System.out.println("RNA_struct_iterator_property: null ("+type.identifier+") "+type.getClass().getName());
		Thread.dumpStack();
	}
	
	return type.iteratorproperty;
}

//StructRNA *RNA_struct_base(StructRNA *type)
//{
//	return type.base;
//}
//
//int RNA_struct_is_ID(StructRNA *type)
//{
//	return (type.flag & STRUCT_ID) != 0;
//}

public static boolean RNA_struct_is_a(StructRNA type, StructRNA srna)
{
	StructRNA base;

	if(type==null)
		return false;

	/* ptr.type is always maximally refined */
	for(base=type; base!=null; base=base.base)
		if(base == srna)
			return true;

	return false;
}

//public static PropertyRNA RNA_struct_find_property(PointerRNA _ptr, String identifier) {
//	return (PropertyRNA)RNA_struct_find_property(_ptr, identifier, false);
//}

public static PropertyRNA RNA_struct_find_property(PointerRNA ptr, byte[] identifier, int offset)
{
	if(identifier[offset+0]=='[' && identifier[offset+1]=='"') { // "  (dummy comment to avoid confusing some function lists in text editors)
		/* id prop lookup, not so common */
		PropertyRNA[] r_prop= {null};
		PointerRNA[] r_ptr = {new PointerRNA()}; /* only support single level props */
		Object ret = RNA_path_resolve(ptr, new byte[][]{identifier}, new int[]{offset}, r_ptr, r_prop);
		if(ret!=null && r_ptr[0].type==ptr.type && r_ptr[0].data==ptr.data)
			return r_prop[0];
	}
	else {
		/* most common case */
		PropertyRNA iterprop= RNA_struct_iterator_property(ptr.type);
		PointerRNA[] propptr = {new PointerRNA()};

		if(RNA_property_collection_lookup_string(ptr, iterprop, identifier,offset, propptr))
			return (PropertyRNA)propptr[0].data;
	}
	
	return null;
}

public static Object RNA_struct_find_property(Object _ptr, String identifier, boolean old)
{
	if (old) { // TMP
		return ((PointerRNA)_ptr).attr.get(identifier);
	}
//	else {
//		if (identifier.equals("use_antialiasing")) {
//			System.out.println("use_antialiasing: "+((PointerRNA)_ptr).type);
//			Thread.dumpStack();
//		}
	
		PointerRNA ptr = (PointerRNA)_ptr;
		PropertyRNA prop = null;
		String className = null;
//		String type = ptr.type.toString();
		if (ptr==null)
			return null;
//		if (ptr.data instanceof RNA_Struct) {
//			RNA_Struct struct = (RNA_Struct)ptr.data;
//			prop = struct.getProperty(identifier);
//			className = struct.getClass().getName();
//		}
//		else {
			/* most common case */
			PropertyRNA iterprop= RNA_struct_iterator_property(ptr.type);
			if (iterprop == null) {
//				System.out.println("RNA_struct_find_property: null iterprop: "+ptr.type);
				return null;
			}
			PointerRNA[] propptr = {null};
	
			if(RNA_property_collection_lookup_string(ptr, iterprop, StringUtil.toCString(identifier),0, propptr))
				return propptr[0].data;
			className = ptr.data.getClass().getName();
//		}
		if (prop != null) {
			return prop;
		}
		else {
//			System.out.println("RNA_struct_find_property: not found: "+className+" - "+identifier+" - "+ptr.type);
//			Thread.dumpStack();
//			EnumPropertyRNA enumProp= new EnumPropertyRNA();
//			enumProp.identifier = "display_mode";
//			enumProp.type = RNATypes.PROP_ENUM;
//			enumProp.subtype = RNATypes.PROP_NONE;
//			enumProp.item = RnaSpaceUtil.display_mode_items;
//			enumProp.name = StringUtil.toCString("Display Mode");
//			enumProp.description = "Type of information to display";
//			enumProp.totarraylength = 1;
//			return enumProp;
			return null;
		}

//    if(identifier[0]=='[' && identifier[1]=='"') { // "  (dummy comment to avoid confusing some function lists in text editors)
//		/* id prop lookup, not so common */
//		PropertyRNA *r_prop= NULL;
//		PointerRNA r_ptr; /* only support single level props */
//		if(RNA_path_resolve(ptr, identifier, &r_ptr, &r_prop) && r_ptr.type==ptr->type && r_ptr.data==ptr->data)
//			return r_prop;
//	}
//	else {
		/* most common case */
//		PropertyRNA iterprop= RNA_struct_iterator_property(ptr.type);
//		PointerRNA[] propptr = {null};
//
//		if(RNA_property_collection_lookup_string(ptr, iterprop, identifier, propptr))
//			return propptr[0].data;
//	}
	
//	return null;
		
//	}
}

///* Find the property which uses the given nested struct */
//PropertyRNA *RNA_struct_find_nested(PointerRNA *ptr, StructRNA *srna)
//{
//	PropertyRNA *prop= NULL;
//
//	RNA_STRUCT_BEGIN(ptr, iprop) {
//		/* This assumes that there can only be one user of this nested struct */
//		if (RNA_property_pointer_type(ptr, iprop) == srna) {
//			prop= iprop;
//			break;
//		}
//	}
//	RNA_PROP_END;
//
//	return prop;
//}
//
//const struct ListBase *RNA_struct_defined_properties(StructRNA *srna)
//{
//	return &srna.cont.properties;
//}

private static ListBaseUtil.OffsetOf offset_FunctionRNA_identifier = new ListBaseUtil.OffsetOf() {
	@Override
	public String get(Link link) {
		return ((FunctionRNA)link).identifier;
	}
};
public static FunctionRNA RNA_struct_find_function(PointerRNA ptr, String identifier)
{
	return new FunctionRNA(identifier);
//#if 1
//	FunctionRNA func;
//	StructRNA type;
//	for(type= ptr.type; type!=null; type= type.base) {
//		func= (FunctionRNA)ListBaseUtil.BLI_findstring_ptr(type.functions, identifier, offset_FunctionRNA_identifier);
//		if(func!=null) {
//			return func;
//		}
//	}
//	return null;

	/* funcitonal but slow */
//#else
//	PointerRNA tptr;
//	PropertyRNA *iterprop;
//	FunctionRNA *func;
//
//	RNA_pointer_create(NULL, &RNA_Struct, ptr->type, &tptr);
//	iterprop= RNA_struct_find_property(&tptr, "functions");
//
//	func= NULL;
//
//	RNA_PROP_BEGIN(&tptr, funcptr, iterprop) {
//		if(strcmp(identifier, RNA_function_identifier(funcptr.data)) == 0) {
//			func= funcptr.data;
//			break;
//		}
//	}
//	RNA_PROP_END;
//
//	return func;
//#endif
}

public static ListBase RNA_struct_defined_functions(StructRNA srna)
{
	return srna.functions;
}

public static StructRegisterFunc RNA_struct_register(StructRNA type)
{
	return type.reg;
}

//StructUnregisterFunc RNA_struct_unregister(StructRNA *type)
//{
//	do {
//		if(type.unreg)
//			return type.unreg;
//	} while((type=type.base));
//
//	return NULL;
//}

public static Object RNA_struct_py_type_get(StructRNA srna)
{
	return srna.py_type;
}

public static void RNA_struct_py_type_set(StructRNA srna, Object py_type)
{
	srna.py_type= py_type;
}

//void *RNA_struct_blender_type_get(StructRNA *srna)
//{
//	return srna.blender_type;
//}
//
//void RNA_struct_blender_type_set(StructRNA *srna, void *blender_type)
//{
//	srna.blender_type= blender_type;
//}

//public static String RNA_struct_name_get_alloc(PointerRNA ptr, String fixedbuf, int fixedlen)
//{
//	PropertyRNA nameprop;
//
//	if(ptr.data!=null && (nameprop = RNA_struct_name_property(ptr.type))!=null)
//		return RNA_property_string_get_alloc(ptr, nameprop, fixedbuf, fixedlen);
//
//	return null;
//}

/* Property Information */

public static String RNA_property_identifier(PropertyRNA prop)
{
//	return rna_ensure_property_identifier(prop);
	return prop.identifier;
}

public static String RNA_property_description(PropertyRNA prop)
{
//	return rna_ensure_property_description(prop);
	return prop.description;
}

//PropertyType RNA_property_type(PropertyRNA *prop)
public static int RNA_property_type(PropertyRNA prop)
{
//	return rna_ensure_property(prop).type;
	return prop.type;
}

//PropertySubType RNA_property_subtype(PropertyRNA *prop)
public static int RNA_property_subtype(PropertyRNA prop)
{
//	return rna_ensure_property(prop).subtype;
	return prop.subtype;
}

public static int RNA_property_flag(PropertyRNA prop)
{
//	return rna_ensure_property(prop).flag;
	return prop.flag;
}

public static int RNA_property_array_length(PointerRNA ptr, PropertyRNA prop)
{
//	return rna_ensure_property_array_length(ptr, prop);
	return prop.totarraylength;
}

/* used by BPY to make an array from the python object */
public static int RNA_property_array_dimension(PointerRNA ptr, PropertyRNA prop, int[] length)
{
//	PropertyRNA rprop= rna_ensure_property(prop);
	PropertyRNA rprop= prop;

//	if(length!=null && rprop.arraydimension > 1)
//		rna_ensure_property_multi_array_length(ptr, prop, length);

	return rprop.arraydimension;
}

public static byte RNA_property_array_item_char(PropertyRNA prop, int index)
{
	final String vectoritem= "XYZW";
	final String quatitem= "WXYZ";
	final String coloritem= "RGBA";
//	PropertySubType subtype= rna_ensure_property(prop)->subtype;
	int subtype= prop.subtype;

	/* get string to use for array index */
	if ((index < 4) && (subtype == RNATypes.PROP_QUATERNION || subtype == RNATypes.PROP_AXISANGLE))
		return (byte)quatitem.charAt(index);
	else if((index < 4) && (subtype == RNATypes.PROP_TRANSLATION || subtype == RNATypes.PROP_DIRECTION || subtype == RNATypes.PROP_XYZ || subtype == RNATypes.PROP_XYZ_LENGTH || subtype == RNATypes.PROP_EULER || subtype == RNATypes.PROP_VELOCITY || subtype == RNATypes.PROP_ACCELERATION))
		return (byte)vectoritem.charAt(index);
	else if ((index < 4) && (subtype == RNATypes.PROP_COLOR || subtype == RNATypes.PROP_COLOR_GAMMA))
		return (byte)coloritem.charAt(index);

	return '\0';
}

public static int RNA_property_array_item_index(PropertyRNA prop, char name)
{
//	PropertySubType subtype= rna_ensure_property(prop).subtype;
	int subtype= rna_ensure_property(prop).subtype;

	name= Character.toUpperCase(name);

	/* get index based on string name/alias */
	/* maybe a function to find char index in string would be better than all the switches */
	if (subtype == RNATypes.PROP_QUATERNION || subtype == RNATypes.PROP_AXISANGLE) {
		switch (name) {
			case 'W':
				return 0;
			case 'X':
				return 1;
			case 'Y':
				return 2;
			case 'Z':
				return 3;
		}
	}
	else if(subtype == RNATypes.PROP_TRANSLATION || subtype == RNATypes.PROP_DIRECTION || subtype == RNATypes.PROP_XYZ || subtype == RNATypes.PROP_EULER || subtype == RNATypes.PROP_VELOCITY || subtype == RNATypes.PROP_ACCELERATION) {
		switch (name) {
			case 'X':
				return 0;
			case 'Y':
				return 1;
			case 'Z':
				return 2;
			case 'W':
				return 3;
		}
	}
	else if (subtype == RNATypes.PROP_COLOR || subtype == RNATypes.PROP_COLOR_GAMMA) {
		switch (name) {
			case 'R':
				return 0;
			case 'G':
				return 1;
			case 'B':
				return 2;
			case 'A':
				return 3;
		}
	}

	return -1;
}

public static void RNA_property_int_range(PointerRNA ptr, PropertyRNA prop, int[] hardmin, int[] hardmax)
{
	IntPropertyRNA iprop= (IntPropertyRNA)rna_ensure_property(prop);

	if(iprop.range!=null) {
		iprop.range.rangeInt(ptr, hardmin, hardmax);
	}
	else {
		hardmin[0]= iprop.hardmin;
		hardmax[0]= iprop.hardmax;
	}
}

public static void RNA_property_int_ui_range(PointerRNA ptr, PropertyRNA prop, int[] softmin, int[] softmax, int[] step)
{
	IntPropertyRNA iprop= (IntPropertyRNA)rna_ensure_property(prop);
	int[] hardmin={0}, hardmax={0};

	if(iprop.range!=null) {
		iprop.range.rangeInt(ptr, hardmin, hardmax);
		softmin[0]= UtilDefines.MAX2(iprop.softmin, hardmin[0]);
		softmax[0]= UtilDefines.MIN2(iprop.softmax, hardmax[0]);
	}
	else {
		softmin[0]= iprop.softmin;
		softmax[0]= iprop.softmax;
	}

	step[0]= iprop.step;
}

public static void RNA_property_float_range(PointerRNA ptr, PropertyRNA prop, float[] hardmin, float[] hardmax)
{
	FloatPropertyRNA fprop= (FloatPropertyRNA)rna_ensure_property(prop);

	if(fprop.range!=null) {
		fprop.range.rangeFloat(ptr, hardmin, hardmax);
	}
	else {
		hardmin[0]= fprop.hardmin;
		hardmax[0]= fprop.hardmax;
	}
}

public static void RNA_property_float_ui_range(PointerRNA ptr, PropertyRNA prop, float[] softmin, float[] softmax, float[] step, float[] precision)
{
	FloatPropertyRNA fprop= (FloatPropertyRNA)rna_ensure_property(prop);
	float[] hardmin={0}, hardmax={0};

	if(fprop.range!=null) {
		fprop.range.rangeFloat(ptr, hardmin, hardmax);
		softmin[0]= UtilDefines.MAX2(fprop.softmin, hardmin[0]);
		softmax[0]= UtilDefines.MIN2(fprop.softmax, hardmax[0]);
	}
	else {
		softmin[0]= fprop.softmin;
		softmax[0]= fprop.softmax;
	}

	step[0]= fprop.step;
	precision[0]= (float)fprop.precision;
}

public static int RNA_property_string_maxlength(PropertyRNA prop)
{
	StringPropertyRNA sprop= (StringPropertyRNA)rna_ensure_property(prop);
	return sprop.maxlength;
}

public static StructRNA RNA_property_pointer_type(PointerRNA ptr, PropertyRNA prop)
{
//	prop= rna_ensure_property(prop);

	if(prop.type == RNATypes.PROP_POINTER) {
		PointerPropertyRNA pprop= (PointerPropertyRNA)prop;

//		if(pprop.typef!=null)
//			return pprop.typef(ptr);
//		else if(pprop.type!=null)
//			return pprop.type;
		if(pprop.type!=null)
			return pprop.type;
	}
	else if(prop.type == RNATypes.PROP_COLLECTION) {
		CollectionPropertyRNA cprop= (CollectionPropertyRNA)prop;

		if(cprop.item_type!=null)
			return cprop.item_type;
	}

	return RNA_UnknownType;
}

public static void RNA_property_enum_items(bContext C, PointerRNA ptr, PropertyRNA prop, EnumPropertyItem[][] item, int[] totitem, int[] free)
{
	EnumPropertyRNA eprop= (EnumPropertyRNA)rna_ensure_property(prop);

	free[0]= 0;

	if(eprop.itemf!=null) {
		int tot= 0;
		item[0]= eprop.itemf.itemEnum(C, ptr, free);

		if(totitem!=null) {
			if(item[0]!=null) {
				for( ; item[0][tot].identifier!=null; tot++);
			}

			totitem[0]= tot;
		}
	}
	else {
		item[0]= eprop.item;
		if(totitem!=null)
			totitem[0]= eprop.totitem;
	}
}

//int RNA_property_enum_value(bContext *C, PointerRNA *ptr, PropertyRNA *prop, const char *identifier, int *value)
//{
//	EnumPropertyItem *item;
//	int free;
//
//	RNA_property_enum_items(C, ptr, prop, &item, NULL, &free);
//
//	for(; item.identifier; item++) {
//		if(item.identifier[0] && strcmp(item.identifier, identifier)==0) {
//			*value = item.value;
//			return 1;
//		}
//	}
//
//	if(free)
//		MEM_freeN(item);
//
//	return 0;
//}
//
//int RNA_enum_identifier(EnumPropertyItem *item, const int value, const char **identifier)
//{
//	for (; item.identifier; item++) {
//		if(item.identifier[0] && item.value==value) {
//			*identifier = item.identifier;
//			return 1;
//		}
//	}
//	return 0;
//}

public static boolean RNA_enum_name(EnumPropertyItem[] item, int value, String[] name)
{
	for (int item_p=0; item[item_p].identifier!=null; item_p++) {
		if(!item[item_p].identifier.isEmpty() && item[item_p].value==value) {
			name[0] = item[item_p].name;
			return true;
		}
	}
	return false;
}

//int RNA_property_enum_identifier(bContext *C, PointerRNA *ptr, PropertyRNA *prop, const int value, const char **identifier)
//{
//	EnumPropertyItem *item= NULL;
//	int result, free;
//
//	RNA_property_enum_items(C, ptr, prop, &item, NULL, &free);
//	if(item) {
//		result= RNA_enum_identifier(item, value, identifier);
//		if(free)
//			MEM_freeN(item);
//
//		return result;
//	}
//	return 0;
//}

public static String RNA_property_ui_name(PropertyRNA prop)
{
//	return rna_ensure_property_name(prop);
	return prop.name;
}

public static String RNA_property_ui_description(PropertyRNA prop)
{
//	return rna_ensure_property(prop).description;
	return prop.description;
}

public static int RNA_property_ui_icon(PropertyRNA prop)
{
//	return rna_ensure_property(prop).icon;
	return prop.icon;
}

public static boolean RNA_property_editable(PointerRNA ptr, PropertyRNA prop)
{
	ID id;
	int flag;

	prop= rna_ensure_property(prop);

	if(prop.editable!=null)
		flag= prop.editable.edit(ptr);
	else
		flag= prop.flag;

	id= (ID)((IDPointer)ptr.id).data;

	return (flag & RNATypes.PROP_EDITABLE)!=0 && (id==null || id.lib==null || (flag & RNATypes.PROP_LIB_EXCEPTION)!=0);
}

//int RNA_property_animateable(PointerRNA *ptr, PropertyRNA *prop)
//{
//	int flag;
//
//	prop= rna_ensure_property(prop);
//
//	if(!(prop.flag & PROP_ANIMATEABLE))
//		return 0;
//
//	if(prop.editable)
//		flag= prop.editable(ptr);
//	else
//		flag= prop.flag;
//
//	return (flag & PROP_EDITABLE);
//}
//
//int RNA_property_animated(PointerRNA *ptr, PropertyRNA *prop)
//{
//	/* would need to ask animation system */
//
//	return 0;
//}
//
//void RNA_property_update(bContext *C, PointerRNA *ptr, PropertyRNA *prop)
//{
//	prop= rna_ensure_property(prop);
//
//	if(prop.update)
//		prop.update(C, ptr);
//	if(prop.noteflag)
//		WM_event_add_notifier(C, prop.noteflag, ptr.id.data);
//}

/* Property Data */

public static int RNA_property_boolean_get(PointerRNA ptr, PropertyRNA prop)
{
	BooleanPropertyRNA bprop= (BooleanPropertyRNA)prop;
//	IDProperty idprop;

//	if((idprop=rna_idproperty_check(&prop, ptr))!=null)
//		return IDP_Int(idprop);
//	else if(bprop.get!=null)
	if(bprop.get!=null)
		return bprop.get.getBool(ptr)?1:0;
	else
		return bprop.defaultvalue?1:0;
}

public static void RNA_property_boolean_set(PointerRNA ptr, PropertyRNA prop, int value)
{
	BooleanPropertyRNA bprop= (BooleanPropertyRNA)prop;
//	IDProperty *idprop;

//	if((idprop=rna_idproperty_check(&prop, ptr)))
//		IDP_Int(idprop)= value;
//	else if(bprop.set)
	if(bprop.set!=null)
		bprop.set.setBool(ptr, value!=0?true:false);
//	else if(prop.flag & PROP_EDITABLE) {
//		IDPropertyTemplate val = {0};
//		IDProperty *group;
//
//		val.i= value;
//
//		group= RNA_struct_idproperties(ptr, 1);
//		if(group)
//			IDP_AddToGroup(group, IDP_New(IDP_INT, val, (char*)prop.identifier));
//	}
}

public static void RNA_property_boolean_get_array(PointerRNA ptr, PropertyRNA prop, boolean[] values)
{
	BooleanPropertyRNA bprop= (BooleanPropertyRNA)prop;
	IDProperty idprop;

	Object[] props = {prop};
	idprop=rna_idproperty_check(props, ptr);
	prop = (PropertyRNA)props[0];
	if(idprop!=null) {
		if(prop.arraylength[0] == 0)
			values[0]= RNA_property_boolean_get(ptr, prop)!=0?true:false;
		else
//			memcpy(values, IDP_Array(idprop), sizeof(int)*idprop.len);
			System.arraycopy(IdProp.IDP_Array(idprop), 0, values, 0, idprop.len);
	}
	else if(prop.arraylength[0] == 0)
		values[0]= RNA_property_boolean_get(ptr, prop)!=0?true:false;
	else if(bprop.getarray!=null)
		bprop.getarray.getBoolArray(ptr, values);
	else if(bprop.defaultarray!=null)
//		memcpy(values, bprop.defaultarray, sizeof(int)*prop.arraylength);
		System.arraycopy(bprop.defaultarray, 0, values, 0, prop.arraylength[0]);
	else
//		memset(values, 0, sizeof(int)*prop.arraylength);
		Arrays.fill(values, false);
}

public static boolean RNA_property_boolean_get_index(PointerRNA ptr, PropertyRNA prop, int index)
{
	boolean[] tmp=new boolean[rna_internal_types.RNA_MAX_ARRAY];

	RNA_property_boolean_get_array(ptr, prop, tmp);
	return tmp[index];
}

//void RNA_property_boolean_set_array(PointerRNA *ptr, PropertyRNA *prop, const int *values)
//{
//	BooleanPropertyRNA *bprop= (BooleanPropertyRNA*)prop;
//	IDProperty *idprop;
//
//	if((idprop=rna_idproperty_check(&prop, ptr))) {
//		if(prop.arraylength == 0)
//			IDP_Int(idprop)= values[0];
//		else
//			memcpy(IDP_Array(idprop), values, sizeof(int)*idprop.len);
//	}
//	else if(prop.arraylength == 0)
//		RNA_property_boolean_set(ptr, prop, values[0]);
//	else if(bprop.setarray)
//		bprop.setarray(ptr, values);
//	else if(prop.flag & PROP_EDITABLE) {
//		IDPropertyTemplate val = {0};
//		IDProperty *group;
//
//		val.array.len= prop.arraylength;
//		val.array.type= IDP_INT;
//
//		group= RNA_struct_idproperties(ptr, 1);
//		if(group) {
//			idprop= IDP_New(IDP_ARRAY, val, (char*)prop.identifier);
//			IDP_AddToGroup(group, idprop);
//			memcpy(IDP_Array(idprop), values, sizeof(int)*idprop.len);
//		}
//	}
//}

public static void RNA_property_boolean_set_index(PointerRNA ptr, PropertyRNA prop, int index, int value)
{
//	int tmp[RNA_MAX_ARRAY];
//
//	RNA_property_boolean_get_array(ptr, prop, tmp);
//	tmp[index]= value;
//	RNA_property_boolean_set_array(ptr, prop, tmp);
}

public static int RNA_property_int_get(PointerRNA ptr, PropertyRNA prop)
{
	IntPropertyRNA iprop= (IntPropertyRNA)prop;
	IDProperty idprop;

	Object[] props = {prop};
	idprop = rna_idproperty_check(props, ptr);
	prop = (PropertyRNA)props[0];
	if(idprop!=null) {
		return IdProp.IDP_Int(idprop);
    }
	else if(iprop.get!=null) {
		return iprop.get.getInt(ptr);
    }
	else {
		return iprop.defaultvalue;
    }
}

public static void RNA_property_int_set(PointerRNA ptr, PropertyRNA prop, int value)
{
	IntPropertyRNA iprop= (IntPropertyRNA)prop;
	IDProperty idprop;

	Object[] props = {prop};
	idprop = rna_idproperty_check(props, ptr);
	prop = (PropertyRNA)props[0];
	if(idprop!=null) {
//		System.out.println("RNA_property_int_set idprop: "+value);
		IdProp.IDP_Int(idprop, value);
	}
	else if(iprop.set!=null) {
//		System.out.println("RNA_property_int_set set: ("+prop.identifier+") "+value);
		iprop.set.setInt(ptr, value);
	}
	else if((prop.flag & RNATypes.PROP_EDITABLE)!=0) {
//		System.out.println("RNA_property_int_set else: "+value);
		IDPropertyTemplate val = new IDPropertyTemplate();
		IDProperty group;

		val.i(value);

		group= RNA_struct_idprops(ptr, true);
		if(group!=null)
			IdProp.IDP_AddToGroup(group, IdProp.IDP_New(DNA_ID.IDP_INT, val, prop.identifier));
	}
}

public static void RNA_property_int_get_array(PointerRNA ptr, PropertyRNA prop, int[] values)
{
	IntPropertyRNA iprop= (IntPropertyRNA)prop;
	IDProperty idprop;

	Object[] props = {prop};
	idprop=rna_idproperty_check(props, ptr);
	prop = (PropertyRNA)props[0];
	if(idprop!=null) {
		if(prop.arraylength[0] == 0)
			values[0]= RNA_property_int_get(ptr, prop);
		else
//			memcpy(values, IDP_Array(idprop), sizeof(int)*idprop.len);
			System.arraycopy(IdProp.IDP_Array(idprop), 0, values, 0, idprop.len);
	}
	else if(prop.arraylength[0] == 0)
		values[0]= RNA_property_int_get(ptr, prop);
	else if(iprop.getarray!=null)
		iprop.getarray.getIntArray(ptr, values);
	else if(iprop.defaultarray!=null)
//		memcpy(values, iprop.defaultarray, sizeof(int)*prop.arraylength);
		System.arraycopy(iprop.defaultarray, 0, values, 0, prop.arraylength[0]);
	else
//		memset(values, 0, sizeof(int)*prop.arraylength);
		Arrays.fill(values, 0);
}

public static int RNA_property_int_get_index(PointerRNA ptr, PropertyRNA prop, int index)
{
	int[] tmp=new int[rna_internal_types.RNA_MAX_ARRAY];

	RNA_property_int_get_array(ptr, prop, tmp);
	return tmp[index];
}

//void RNA_property_int_set_array(PointerRNA *ptr, PropertyRNA *prop, const int *values)
//{
//	IntPropertyRNA *iprop= (IntPropertyRNA*)prop;
//	IDProperty *idprop;
//
//	if((idprop=rna_idproperty_check(&prop, ptr))) {
//		if(prop.arraylength == 0)
//			IDP_Int(idprop)= values[0];
//		else
//			memcpy(IDP_Array(idprop), values, sizeof(int)*idprop.len);\
//	}
//	else if(prop.arraylength == 0)
//		RNA_property_int_set(ptr, prop, values[0]);
//	else if(iprop.setarray)
//		iprop.setarray(ptr, values);
//	else if(prop.flag & PROP_EDITABLE) {
//		IDPropertyTemplate val = {0};
//		IDProperty *group;
//
//		val.array.len= prop.arraylength;
//		val.array.type= IDP_INT;
//
//		group= RNA_struct_idproperties(ptr, 1);
//		if(group) {
//			idprop= IDP_New(IDP_ARRAY, val, (char*)prop.identifier);
//			IDP_AddToGroup(group, idprop);
//			memcpy(IDP_Array(idprop), values, sizeof(int)*idprop.len);
//		}
//	}
//}

public static void RNA_property_int_set_index(PointerRNA ptr, PropertyRNA prop, int index, int value)
{
//	int tmp[RNA_MAX_ARRAY];
//
//	RNA_property_int_get_array(ptr, prop, tmp);
//	tmp[index]= value;
//	RNA_property_int_set_array(ptr, prop, tmp);
}

public static float RNA_property_float_get(PointerRNA ptr, PropertyRNA prop)
{
	FloatPropertyRNA fprop= (FloatPropertyRNA)prop;
//	IDProperty *idprop;

//	if((idprop=rna_idproperty_check(&prop, ptr))) {
//		if(idprop.type == IDP_FLOAT)
//			return IDP_Float(idprop);
//		else
//			return (float)IDP_Double(idprop);
//	}
//	else if(fprop.get)
	if(fprop.get!=null)
		return fprop.get.getFloat(ptr);
	else
		return fprop.defaultvalue;
}

public static void RNA_property_float_set(PointerRNA ptr, PropertyRNA prop, float value)
{
	FloatPropertyRNA fprop= (FloatPropertyRNA)prop;
//	IDProperty *idprop;

//	if((idprop=rna_idproperty_check(&prop, ptr))) {
//		if(idprop.type == IDP_FLOAT)
//			IDP_Float(idprop)= value;
//		else
//			IDP_Double(idprop)= value;
//	}
//	else if(fprop.set) {
	if(fprop.set!=null) {
		fprop.set.setFloat(ptr, value);
	}
//	else if(prop.flag & PROP_EDITABLE) {
//		IDPropertyTemplate val = {0};
//		IDProperty *group;
//
//		val.f= value;
//
//		group= RNA_struct_idproperties(ptr, 1);
//		if(group)
//			IDP_AddToGroup(group, IDP_New(IDP_FLOAT, val, (char*)prop.identifier));
//	}
}

public static void RNA_property_float_get_array(PointerRNA ptr, PropertyRNA prop, float[] values)
{
	FloatPropertyRNA fprop= (FloatPropertyRNA)prop;
	IDProperty idprop;
	int i;

	Object[] props = {prop};
	idprop=rna_idproperty_check(props, ptr);
	prop = (PropertyRNA)props[0];
	if(idprop!=null) {
		if(prop.arraylength[0] == 0)
			values[0]= RNA_property_float_get(ptr, prop);
		else if(idprop.subtype == DNA_ID.IDP_FLOAT) {
//			memcpy(values, IDP_Array(idprop), sizeof(float)*idprop.len);
			System.arraycopy(IdProp.IDP_Array(idprop), 0, values, 0, idprop.len);
		}
		else {
			for(i=0; i<idprop.len; i++)
//				values[i]=  (float)(((double*)IDP_Array(idprop))[i]);
				values[i]=  (float)(((double[])IdProp.IDP_Array(idprop))[i]);
		}
	}
	else if(prop.arraylength[0] == 0)
		values[0]= RNA_property_float_get(ptr, prop);
	else if(fprop.getarray!=null)
		fprop.getarray.getFloatArray(ptr, values);
	else if(fprop.defaultarray!=null)
//		memcpy(values, fprop.defaultarray, sizeof(float)*prop.arraylength);
		System.arraycopy(fprop.defaultarray, 0, values, 0, prop.arraylength[0]);
	else
//		memset(values, 0, sizeof(float)*prop.arraylength);
		Arrays.fill(values, 0.0f);
}

public static float RNA_property_float_get_index(PointerRNA ptr, PropertyRNA prop, int index)
{
	float[] tmp=new float[rna_internal_types.RNA_MAX_ARRAY];

	RNA_property_float_get_array(ptr, prop, tmp);
	return tmp[index];
}

//void RNA_property_float_set_array(PointerRNA *ptr, PropertyRNA *prop, const float *values)
//{
//	FloatPropertyRNA *fprop= (FloatPropertyRNA*)prop;
//	IDProperty *idprop;
//	int i;
//
//	if((idprop=rna_idproperty_check(&prop, ptr))) {
//		if(prop.arraylength == 0)
//			IDP_Double(idprop)= values[0];
//		else if(idprop.subtype == IDP_FLOAT) {
//			memcpy(IDP_Array(idprop), values, sizeof(float)*idprop.len);
//		}
//		else {
//			for(i=0; i<idprop.len; i++)
//				((double*)IDP_Array(idprop))[i]= values[i];
//		}
//	}
//	else if(prop.arraylength == 0)
//		RNA_property_float_set(ptr, prop, values[0]);
//	else if(fprop.setarray) {
//		fprop.setarray(ptr, values);
//	}
//	else if(prop.flag & PROP_EDITABLE) {
//		IDPropertyTemplate val = {0};
//		IDProperty *group;
//
//		val.array.len= prop.arraylength;
//		val.array.type= IDP_FLOAT;
//
//		group= RNA_struct_idproperties(ptr, 1);
//		if(group) {
//			idprop= IDP_New(IDP_ARRAY, val, (char*)prop.identifier);
//			IDP_AddToGroup(group, idprop);
//			memcpy(IDP_Array(idprop), values, sizeof(float)*idprop.len);
//		}
//	}
//}

public static void RNA_property_float_set_index(PointerRNA ptr, PropertyRNA prop, int index, float value)
{
//	float tmp[RNA_MAX_ARRAY];
//
//	RNA_property_float_get_array(ptr, prop, tmp);
//	tmp[index]= value;
//	RNA_property_float_set_array(ptr, prop, tmp);
}

//public static void RNA_property_string_get(PointerRNA ptr, PropertyRNA prop, char *value)
public static void RNA_property_string_get(PointerRNA ptr, PropertyRNA prop, byte[] value, int offset)
{
	StringPropertyRNA sprop= (StringPropertyRNA)prop;
//	IDProperty idprop;

//	if((idprop=rna_idproperty_check(&prop, ptr)))
//		strcpy(value, IDP_String(idprop));
//	else
	if(sprop.get!=null)
//		sprop.get.get(ptr, value);
		sprop.get.getStr(ptr, value, offset);
	else
//		strcpy(value, sprop.defaultvalue);
		StringUtil.strcpy(value,offset, StringUtil.toCString(sprop.defaultvalue),0);
}

//public String RNA_property_string_get_alloc(PointerRNA ptr, PropertyRNA prop, String fixedbuf, int fixedlen)
public static byte[] RNA_property_string_get_alloc(PointerRNA ptr, PropertyRNA prop, byte[] fixedbuf, int fixedlen)
{
	byte[] buf;
	int length;

	length= RNA_property_string_length(ptr, prop);

	if(length+1 < fixedlen)
		buf= fixedbuf;
	else
//		buf= MEM_callocN(sizeof(char)*(length+1), "RNA_string_get_alloc");
		buf= new byte[length+1];

//	RNA_property_string_get(ptr, prop, buf);
	RNA_property_string_get(ptr, prop, buf,0);

	return buf;
}

public static int RNA_property_string_length(PointerRNA ptr, PropertyRNA prop)
{
	StringPropertyRNA sprop= (StringPropertyRNA)prop;
//	IDProperty *idprop;

//	if((idprop=rna_idproperty_check(&prop, ptr)))
//		return strlen(IDP_String(idprop));
//	else
	if(sprop.length!=null)
		return sprop.length.lenStr(ptr);
	else
		return StringUtil.strlen(StringUtil.toCString(sprop.defaultvalue),0);
}

//void RNA_property_string_set(PointerRNA *ptr, PropertyRNA *prop, const char *value)
//{
//	StringPropertyRNA *sprop= (StringPropertyRNA*)prop;
//	IDProperty *idprop;
//
//	if((idprop=rna_idproperty_check(&prop, ptr)))
//		IDP_AssignString(idprop, (char*)value);
//	else if(sprop.set)
//		sprop.set(ptr, value);
//	else if(prop.flag & PROP_EDITABLE) {
//		IDPropertyTemplate val = {0};
//		IDProperty *group;
//
//		val.str= (char*)value;
//
//		group= RNA_struct_idproperties(ptr, 1);
//		if(group)
//			IDP_AddToGroup(group, IDP_New(IDP_STRING, val, (char*)prop.identifier));
//	}
//}

public static int RNA_property_enum_get(PointerRNA ptr, PropertyRNA prop)
{
	EnumPropertyRNA eprop= (EnumPropertyRNA)prop;
//	IDProperty *idprop;

//	if((idprop=rna_idproperty_check(&prop, ptr)))
//		return IDP_Int(idprop);
//	else if(eprop.get)
	if(eprop.get!=null)
		return eprop.get.getEnum(ptr);
	else
		return eprop.defaultvalue;
}


public static void RNA_property_enum_set(PointerRNA ptr, PropertyRNA prop, int value)
{
	EnumPropertyRNA eprop= (EnumPropertyRNA)prop;
//	IDProperty *idprop;

//	if((idprop=rna_idproperty_check(&prop, ptr)))
//		IDP_Int(idprop)= value;
//	else
	if(eprop.set!=null) {
		eprop.set.setEnum(ptr, value);
	}
//	else if(prop.flag & PROP_EDITABLE) {
//		IDPropertyTemplate val = {0};
//		IDProperty *group;
//
//		val.i= value;
//
//		group= RNA_struct_idproperties(ptr, 1);
//		if(group)
//			IDP_AddToGroup(group, IDP_New(IDP_INT, val, (char*)prop.identifier));
//	}
}

public static PointerRNA RNA_property_pointer_get(PointerRNA ptr, PropertyRNA prop)
{
//	PointerPropertyRNA pprop= (PointerPropertyRNA)prop;
//	IDProperty idprop;

//	if((idprop=rna_idproperty_check(&prop, ptr))) {
//		pprop= (PointerPropertyRNA*)prop;

		/* for groups, data is idprop itself */
//		return rna_pointer_inherit_refine(ptr, pprop.type, idprop);
//		return ptr;
//	}
//	else if(pprop.get!=null) {
//		return pprop.get(ptr);
//	}
//	else {
//		PointerRNA result;
//		memset(&result, 0, sizeof(result));
//		return result;
		return new PointerRNA();
//	}
}

//void RNA_property_pointer_set(PointerRNA *ptr, PropertyRNA *prop, PointerRNA ptr_value)
//{
//	PointerPropertyRNA *pprop= (PointerPropertyRNA*)prop;
//
//	if(pprop.set)
//		pprop.set(ptr, ptr_value);
//}
//
//void RNA_property_pointer_add(PointerRNA *ptr, PropertyRNA *prop)
//{
//	IDProperty *idprop;
//
//	if((idprop=rna_idproperty_check(&prop, ptr))) {
//		/* already exists */
//	}
//	else if(prop.flag & PROP_IDPROPERTY) {
//		IDPropertyTemplate val = {0};
//		IDProperty *group;
//
//		val.i= 0;
//
//		group= RNA_struct_idproperties(ptr, 1);
//		if(group)
//			IDP_AddToGroup(group, IDP_New(IDP_GROUP, val, (char*)prop.identifier));
//	}
//	else
//		printf("RNA_property_pointer_add %s.%s: only supported for id properties.\n", ptr.type.identifier, prop.identifier);
//}
//
//void RNA_property_pointer_remove(PointerRNA *ptr, PropertyRNA *prop)
//{
//	IDProperty *idprop, *group;
//
//	if((idprop=rna_idproperty_check(&prop, ptr))) {
//		group= RNA_struct_idproperties(ptr, 0);
//
//		if(group) {
//			IDP_RemFromGroup(group, idprop);
//			IDP_FreeProperty(idprop);
//			MEM_freeN(idprop);
//		}
//	}
//	else
//		printf("RNA_property_pointer_remove %s.%s: only supported for id properties.\n", ptr.type.identifier, prop.identifier);
//}

static void rna_property_collection_get_idp(CollectionPropertyIterator iter)
{
	CollectionPropertyRNA cprop= (CollectionPropertyRNA)iter.prop;

	iter.ptr.data= rna_iterator_array_get.get(iter);
	iter.ptr.type= cprop.item_type;
	rna_pointer_inherit_id(cprop.item_type, iter.parent, iter.ptr);
}

public static void RNA_property_collection_begin(PointerRNA ptr, PropertyRNA prop, CollectionPropertyIterator iter)
{
	IDProperty idprop;

//	memset(iter, 0, sizeof(*iter));
	iter.clear();

//	if((idprop=rna_idproperty_check(&prop, ptr)) || (prop->flag & PROP_IDPROPERTY)) {
//		iter->parent= *ptr;
//		iter->prop= prop;
//
//		if(idprop)
//			rna_iterator_array_begin(iter, IDP_IDPArray(idprop), sizeof(IDProperty), idprop->len, 0, NULL);
//		else
//			rna_iterator_array_begin(iter, NULL, sizeof(IDProperty), 0, 0, NULL);
//
//		if(iter->valid)
//			rna_property_collection_get_idp(iter);
//
//		iter->idprop= 1;
//	}
//	else {
		CollectionPropertyRNA cprop= (CollectionPropertyRNA)prop;
		if (cprop.begin == null) {
			System.out.println("RNA_property_collection_begin: null begin: "+cprop.identifier);
		}
		cprop.begin.begin(iter, ptr);
//	}
}

public static void RNA_property_collection_next(CollectionPropertyIterator iter)
{
	CollectionPropertyRNA cprop= (CollectionPropertyRNA)rna_ensure_property(iter.prop);

	if(iter.idprop!=0) {
		rna_iterator_array_next.next(iter);

		if(iter.valid)
			rna_property_collection_get_idp(iter);
	}
	else
		cprop.next.next(iter);
}

public static void RNA_property_collection_end(CollectionPropertyIterator iter)
{
	CollectionPropertyRNA cprop= (CollectionPropertyRNA)rna_ensure_property(iter.prop);

	if(iter.idprop!=0)
		rna_iterator_array_end.end(iter);
	else
		cprop.end.end(iter);
}

//int RNA_property_collection_length(PointerRNA *ptr, PropertyRNA *prop)
//{
//	CollectionPropertyRNA *cprop= (CollectionPropertyRNA*)prop;
//	IDProperty *idprop;
//
//	if((idprop=rna_idproperty_check(&prop, ptr))) {
//		return idprop.len;
//	}
//	else if(cprop.length) {
//		return cprop.length(ptr);
//	}
//	else {
//		CollectionPropertyIterator iter;
//		int length= 0;
//
//		RNA_property_collection_begin(ptr, prop, &iter);
//		for(; iter.valid; RNA_property_collection_next(&iter))
//			length++;
//		RNA_property_collection_end(&iter);
//
//		return length;
//	}
//}
//
//void RNA_property_collection_add(PointerRNA *ptr, PropertyRNA *prop, PointerRNA *r_ptr)
//{
//	IDProperty *idprop;
//	//CollectionPropertyRNA *cprop= (CollectionPropertyRNA*)prop;
//
//	if((idprop=rna_idproperty_check(&prop, ptr))) {
//		IDPropertyTemplate val = {0};
//		IDProperty *item;
//
//		item= IDP_New(IDP_GROUP, val, "");
//		IDP_AppendArray(idprop, item);
//		IDP_FreeProperty(item);
//		MEM_freeN(item);
//	}
//	else if(prop.flag & PROP_IDPROPERTY) {
//		IDProperty *group, *item;
//		IDPropertyTemplate val = {0};
//
//		group= RNA_struct_idproperties(ptr, 1);
//		if(group) {
//			idprop= IDP_NewIDPArray(prop.identifier);
//			IDP_AddToGroup(group, idprop);
//
//			item= IDP_New(IDP_GROUP, val, "");
//			IDP_AppendArray(idprop, item);
//			IDP_FreeProperty(item);
//			MEM_freeN(item);
//		}
//	}
//#if 0
//	else if(cprop.add){
//		if(!(cprop.add.flag & FUNC_USE_CONTEXT)) { /* XXX check for this somewhere else */
//			ParameterList params;
//			RNA_parameter_list_create(&params, ptr, cprop.add);
//			RNA_function_call(NULL, NULL, ptr, cprop.add, &params);
//			RNA_parameter_list_free(&params);
//		}
//	}
//#endif
//	else
//		printf("RNA_property_collection_add %s.%s: not implemented for this property.\n", ptr.type.identifier, prop.identifier);
//
//	if(r_ptr) {
//		if(idprop) {
//			CollectionPropertyRNA *cprop= (CollectionPropertyRNA*)prop;
//
//			r_ptr.data= IDP_GetIndexArray(idprop, idprop.len-1);
//			r_ptr.type= cprop.type;
//			rna_pointer_inherit_id(NULL, ptr, r_ptr);
//		}
//		else
//			memset(r_ptr, 0, sizeof(*r_ptr));
//	}
//}
//
//void RNA_property_collection_remove(PointerRNA *ptr, PropertyRNA *prop, int key)
//{
//	IDProperty *idprop;
//	//CollectionPropertyRNA *cprop= (CollectionPropertyRNA*)prop;
//
//	if((idprop=rna_idproperty_check(&prop, ptr))) {
//		IDProperty tmp, *array;
//		int len;
//
//		len= idprop.len;
//		array= IDP_IDPArray(idprop);
//
//		if(key >= 0 && key < len) {
//			if(key+1 < len) {
//				/* move element to be removed to the back */
//				memcpy(&tmp, &array[key], sizeof(IDProperty));
//				memmove(array+key, array+key+1, sizeof(IDProperty)*(len-key+1));
//				memcpy(&array[len-1], &tmp, sizeof(IDProperty));
//			}
//
//			IDP_ResizeIDPArray(idprop, len-1);
//		}
//	}
//	else if(prop.flag & PROP_IDPROPERTY);
//#if 0
//	else if(cprop.remove){
//		if(!(cprop.remove.flag & FUNC_USE_CONTEXT)) { /* XXX check for this somewhere else */
//			ParameterList params;
//			RNA_parameter_list_create(&ptr, cprop.remove);
//			RNA_function_call(NULL, NULL, ptr, cprop.remove, &params);
//			RNA_parameter_list_free(&params);
//		}
//	}
//#endif
//	else
//		printf("RNA_property_collection_remove %s.%s: only supported for id properties.\n", ptr.type.identifier, prop.identifier);
//}
//
//void RNA_property_collection_clear(PointerRNA *ptr, PropertyRNA *prop)
//{
//	IDProperty *idprop;
//
//	if((idprop=rna_idproperty_check(&prop, ptr)))
//		IDP_ResizeIDPArray(idprop, 0);
//}

public static boolean RNA_property_collection_lookup_int(PointerRNA ptr, PropertyRNA prop, int key, PointerRNA[] r_ptr)
{
	CollectionPropertyRNA cprop= (CollectionPropertyRNA)prop;

	if(cprop.lookupint!=null) {
		/* we have a callback defined, use it */
		r_ptr[0]= cprop.lookupint.lookupInt(ptr, key);
		return (r_ptr[0].data != null);
	}
	else {
		/* no callback defined, just iterate and find the nth item */
		CollectionPropertyIterator iter = new CollectionPropertyIterator();
		int i;

		RNA_property_collection_begin(ptr, prop, iter);
		for(i=0; iter.valid; RNA_property_collection_next(iter), i++) {
			if(i == key) {
				r_ptr[0]= iter.ptr;
				break;
			}
		}
		RNA_property_collection_end(iter);

		if(!iter.valid)
//			memset(r_ptr, 0, sizeof(*r_ptr));
			r_ptr[0] = null;

		return iter.valid;
	}
}

public static boolean RNA_property_collection_lookup_string(PointerRNA ptr, PropertyRNA prop, byte[] key, int offset, PointerRNA[] r_ptr)
{
	CollectionPropertyRNA cprop= (CollectionPropertyRNA)rna_ensure_property(prop);

	if(cprop.lookupstring!=null) {
		/* we have a callback defined, use it */
		r_ptr[0]= cprop.lookupstring.lookupStr(ptr, StringUtil.toJString(key,offset));
		return (r_ptr[0].data != null);
	}
	else {
		/* no callback defined, compare with name properties if they exist */
		CollectionPropertyIterator iter = new CollectionPropertyIterator();
		PropertyRNA nameprop;
		byte[] name=new byte[256], nameptr;
//		String nameptr;
		boolean found= false;

		RNA_property_collection_begin(ptr, prop, iter);
		for(; iter.valid; RNA_property_collection_next(iter)) {
			if(iter.ptr.data!=null && iter.ptr.type.nameproperty!=null) {
				nameprop= iter.ptr.type.nameproperty;

//				nameptr= RNA_property_string_get_alloc(iter.ptr, nameprop, name, sizeof(name));
				nameptr= RNA_property_string_get_alloc(iter.ptr, nameprop, name, name.length);

//				if(nameptr.equals(key)) {
				if(StringUtil.strcmp(nameptr,0, key,0) == 0) {
					r_ptr[0]= iter.ptr;
					found= true;
				}

//				if((char *)&name != nameptr)
//					MEM_freeN(nameptr);

				if(found)
					break;
			}
		}
		RNA_property_collection_end(iter);

		if(!iter.valid)
//			memset(r_ptr, 0, sizeof(*r_ptr));
			r_ptr[0] = null;

		return iter.valid;
	}
}

public static boolean RNA_property_collection_type_get(PointerRNA ptr, PropertyRNA prop, PointerRNA[] r_ptr)
{
	r_ptr[0]= ptr;
	return (r_ptr[0].type = prop.srna)!=null;
}

//int RNA_property_collection_raw_array(PointerRNA *ptr, PropertyRNA *prop, PropertyRNA *itemprop, RawArray *array)
//{
//	CollectionPropertyIterator iter;
//	ArrayIterator *internal;
//	char *arrayp;
//
//	if(!(prop.flag & PROP_RAW_ARRAY) || !(itemprop.flag & PROP_RAW_ACCESS))
//		return 0;
//
//	RNA_property_collection_begin(ptr, prop, &iter);
//
//	if(iter.valid) {
//		/* get data from array iterator and item property */
//		internal= iter.internal;
//		arrayp= (iter.valid)? iter.ptr.data: NULL;
//
//		if(internal.skip || !RNA_property_editable(&iter.ptr, itemprop)) {
//			/* we might skip some items, so it's not a proper array */
//			RNA_property_collection_end(&iter);
//			return 0;
//		}
//
//		array.array= arrayp + itemprop.rawoffset;
//		array.stride= internal.itemsize;
//		array.len= ((char*)internal.endptr - arrayp)/internal.itemsize;
//		array.type= itemprop.rawtype;
//	}
//	else
//		memset(array, 0, sizeof(RawArray));
//
//	RNA_property_collection_end(&iter);
//
//	return 1;
//}
//
//#define RAW_GET(dtype, var, raw, a) \
//{ \
//	switch(raw.type) { \
//		case PROP_RAW_CHAR: var = (dtype)((char*)raw.array)[a]; break; \
//		case PROP_RAW_SHORT: var = (dtype)((short*)raw.array)[a]; break; \
//		case PROP_RAW_INT: var = (dtype)((int*)raw.array)[a]; break; \
//		case PROP_RAW_FLOAT: var = (dtype)((float*)raw.array)[a]; break; \
//		case PROP_RAW_DOUBLE: var = (dtype)((double*)raw.array)[a]; break; \
//		default: var = (dtype)0; \
//	} \
//}
//
//#define RAW_SET(dtype, raw, a, var) \
//{ \
//	switch(raw.type) { \
//		case PROP_RAW_CHAR: ((char*)raw.array)[a] = (char)var; break; \
//		case PROP_RAW_SHORT: ((short*)raw.array)[a] = (short)var; break; \
//		case PROP_RAW_INT: ((int*)raw.array)[a] = (int)var; break; \
//		case PROP_RAW_FLOAT: ((float*)raw.array)[a] = (float)var; break; \
//		case PROP_RAW_DOUBLE: ((double*)raw.array)[a] = (double)var; break; \
//	} \
//}
//
//int RNA_raw_type_sizeof(RawPropertyType type)
//{
//	switch(type) {
//		case PROP_RAW_CHAR: return sizeof(char);
//		case PROP_RAW_SHORT: return sizeof(short);
//		case PROP_RAW_INT: return sizeof(int);
//		case PROP_RAW_FLOAT: return sizeof(float);
//		case PROP_RAW_DOUBLE: return sizeof(double);
//		default: return 0;
//	}
//}
//
//static int rna_raw_access(ReportList *reports, PointerRNA *ptr, PropertyRNA *prop, char *propname, void *inarray, RawPropertyType intype, int inlen, int set)
//{
//	StructRNA *ptype;
//	PointerRNA itemptr;
//	PropertyRNA *itemprop, *iprop;
//	PropertyType itemtype=0;
//	RawArray in;
//	int itemlen= 0;
//
//	/* initialize in array, stride assumed 0 in following code */
//	in.array= inarray;
//	in.type= intype;
//	in.len= inlen;
//	in.stride= 0;
//
//	ptype= RNA_property_pointer_type(ptr, prop);
//
//	/* try to get item property pointer */
//	RNA_pointer_create(NULL, ptype, NULL, &itemptr);
//	itemprop= RNA_struct_find_property(&itemptr, propname);
//
//	if(itemprop) {
//		/* we have item property pointer */
//		RawArray out;
//
//		/* check type */
//		itemtype= RNA_property_type(itemprop);
//
//		if(!ELEM3(itemtype, PROP_BOOLEAN, PROP_INT, PROP_FLOAT)) {
//			BKE_report(reports, RPT_ERROR, "Only boolean, int and float properties supported.");
//			return 0;
//		}
//
//		/* check item array */
//		itemlen= RNA_property_array_length(itemprop);
//
//		/* try to access as raw array */
//		if(RNA_property_collection_raw_array(ptr, prop, itemprop, &out)) {
//			if(in.len != itemlen*out.len) {
//				BKE_reportf(reports, RPT_ERROR, "Array length mismatch (expected %d, got %d).", out.len*itemlen, in.len);
//				return 0;
//			}
//
//			/* matching raw types */
//			if(out.type == in.type) {
//				void *inp= in.array;
//				void *outp= out.array;
//				int a, size;
//
//				itemlen= (itemlen == 0)? 1: itemlen;
//				size= RNA_raw_type_sizeof(out.type) * itemlen;
//
//				for(a=0; a<out.len; a++) {
//					if(set) memcpy(outp, inp, size);
//					else memcpy(inp, outp, size);
//
//					inp= (char*)inp + size;
//					outp= (char*)outp + out.stride;
//				}
//
//				return 1;
//			}
//
//			/* could also be faster with non-matching types,
//			 * for now we just do slower loop .. */
//		}
//	}
//
//	{
//		void *tmparray= NULL;
//		int tmplen= 0;
//		int err= 0, j, a= 0;
//
//		/* no item property pointer, can still be id property, or
//		 * property of a type derived from the collection pointer type */
//		RNA_PROP_BEGIN(ptr, itemptr, prop) {
//			if(itemptr.data) {
//				if(itemprop) {
//					/* we got the property already */
//					iprop= itemprop;
//				}
//				else {
//					/* not yet, look it up and verify if it is valid */
//					iprop= RNA_struct_find_property(&itemptr, propname);
//
//					if(iprop) {
//						itemlen= RNA_property_array_length(iprop);
//						itemtype= RNA_property_type(iprop);
//					}
//					else {
//						BKE_reportf(reports, RPT_ERROR, "Property named %s not found.", propname);
//						err= 1;
//						break;
//					}
//
//					if(!ELEM3(itemtype, PROP_BOOLEAN, PROP_INT, PROP_FLOAT)) {
//						BKE_report(reports, RPT_ERROR, "Only boolean, int and float properties supported.");
//						err= 1;
//						break;
//					}
//				}
//
//				/* editable check */
//				if(RNA_property_editable(&itemptr, iprop)) {
//					if(a+itemlen > in.len) {
//						BKE_reportf(reports, RPT_ERROR, "Array length mismatch (got %d, expected more).", in.len);
//						err= 1;
//						break;
//					}
//
//					if(itemlen == 0) {
//						/* handle conversions */
//						if(set) {
//							switch(itemtype) {
//								case PROP_BOOLEAN: {
//									int b;
//									RAW_GET(int, b, in, a);
//									RNA_property_boolean_set(&itemptr, iprop, b);
//									break;
//								}
//								case PROP_INT: {
//									int i;
//									RAW_GET(int, i, in, a);
//									RNA_property_int_set(&itemptr, iprop, i);
//									break;
//								}
//								case PROP_FLOAT: {
//									float f;
//									RAW_GET(float, f, in, a);
//									RNA_property_float_set(&itemptr, iprop, f);
//									break;
//								}
//								default:
//									break;
//							}
//						}
//						else {
//							switch(itemtype) {
//								case PROP_BOOLEAN: {
//									int b= RNA_property_boolean_get(&itemptr, iprop);
//									RAW_SET(int, in, a, b);
//									break;
//								}
//								case PROP_INT: {
//									int i= RNA_property_int_get(&itemptr, iprop);
//									RAW_SET(int, in, a, i);
//									break;
//								}
//								case PROP_FLOAT: {
//									float f= RNA_property_float_get(&itemptr, iprop);
//									RAW_SET(float, in, a, f);
//									break;
//								}
//								default:
//									break;
//							}
//						}
//						a++;
//					}
//					else {
//						/* allocate temporary array if needed */
//						if(tmparray && tmplen != itemlen) {
//							MEM_freeN(tmparray);
//							tmparray= NULL;
//						}
//						if(!tmparray) {
//							tmparray= MEM_callocN(sizeof(float)*itemlen, "RNA tmparray\n");
//							tmplen= itemlen;
//						}
//
//						/* handle conversions */
//						if(set) {
//							switch(itemtype) {
//								case PROP_BOOLEAN: {
//									for(j=0; j<itemlen; j++, a++)
//										RAW_GET(int, ((int*)tmparray)[j], in, a);
//									RNA_property_boolean_set_array(&itemptr, iprop, tmparray);
//									break;
//								}
//								case PROP_INT: {
//									for(j=0; j<itemlen; j++, a++)
//										RAW_GET(int, ((int*)tmparray)[j], in, a);
//									RNA_property_int_set_array(&itemptr, iprop, tmparray);
//									break;
//								}
//								case PROP_FLOAT: {
//									for(j=0; j<itemlen; j++, a++)
//										RAW_GET(float, ((float*)tmparray)[j], in, a);
//									RNA_property_float_set_array(&itemptr, iprop, tmparray);
//									break;
//								}
//								default:
//									break;
//							}
//						}
//						else {
//							switch(itemtype) {
//								case PROP_BOOLEAN: {
//									RNA_property_boolean_get_array(&itemptr, iprop, tmparray);
//									for(j=0; j<itemlen; j++, a++)
//										RAW_SET(int, in, a, ((int*)tmparray)[j]);
//									break;
//								}
//								case PROP_INT: {
//									RNA_property_int_get_array(&itemptr, iprop, tmparray);
//									for(j=0; j<itemlen; j++, a++)
//										RAW_SET(int, in, a, ((int*)tmparray)[j]);
//									break;
//								}
//								case PROP_FLOAT: {
//									RNA_property_float_get_array(&itemptr, iprop, tmparray);
//									for(j=0; j<itemlen; j++, a++)
//										RAW_SET(float, in, a, ((float*)tmparray)[j]);
//									break;
//								}
//								default:
//									break;
//							}
//						}
//					}
//				}
//			}
//		}
//		RNA_PROP_END;
//
//		if(tmparray)
//			MEM_freeN(tmparray);
//
//		return !err;
//	}
//}
//
//RawPropertyType RNA_property_raw_type(PropertyRNA *prop)
//{
//	return prop.rawtype;
//}
//
//int RNA_property_collection_raw_get(ReportList *reports, PointerRNA *ptr, PropertyRNA *prop, char *propname, void *array, RawPropertyType type, int len)
//{
//	return rna_raw_access(reports, ptr, prop, propname, array, type, len, 0);
//}
//
//int RNA_property_collection_raw_set(ReportList *reports, PointerRNA *ptr, PropertyRNA *prop, char *propname, void *array, RawPropertyType type, int len)
//{
//	return rna_raw_access(reports, ptr, prop, propname, array, type, len, 1);
//}

/* Standard iterator functions */

public static void rna_iterator_listbase_begin(CollectionPropertyIterator iter, ListBase lb, IteratorSkipFunc skip)
{
//	ListBaseIterator *internal;
//
//	internal= MEM_callocN(sizeof(ListBaseIterator), "ListBaseIterator");
//	internal.link= (lb)? lb.first: NULL;
//	internal.skip= skip;
//
//	iter.internal= internal;
//	iter.valid= (internal.link != NULL);
//
//	if(skip && iter.valid && skip(iter, internal.link))
//		rna_iterator_listbase_next(iter);
}

public static PropCollectionNextFunc rna_iterator_listbase_next = new PropCollectionNextFunc() {
public void next(CollectionPropertyIterator iter)
{
//	ListBaseIterator *internal= iter.internal;
//
//	if(internal.skip) {
//		do {
//			internal.link= internal.link.next;
//			iter.valid= (internal.link != NULL);
//		} while(iter.valid && internal.skip(iter, internal.link));
//	}
//	else {
//		internal.link= internal.link.next;
//		iter.valid= (internal.link != NULL);
//	}
}};

public static PropCollectionGetFunc rna_iterator_listbase_get = new PropCollectionGetFunc() {
public PointerRNA get(CollectionPropertyIterator iter)
{
//	ListBaseIterator internal= iter.internal;
//
//	return internal.link;
	return null;
}};

public static PropCollectionEndFunc rna_iterator_listbase_end = new PropCollectionEndFunc() {
public void end(CollectionPropertyIterator iter)
{
//	MEM_freeN(iter.internal);
	iter.internal= null;
}};

public static void rna_iterator_array_begin(CollectionPropertyIterator iter, Object[] ptr, int itemsize, int length, int free_ptr, IteratorSkipFunc skip)
{
	ArrayIterator internal;

	if(ptr == null)
		length= 0;

//	internal= MEM_callocN(sizeof(ArrayIterator), "ArrayIterator");
	internal= new ArrayIterator();
//	internal.ptr= ptr;
	internal.array= ptr;
	internal.free_ptr= free_ptr!=0 ? ptr:null;
//	internal.endptr= ((char*)ptr)+length*itemsize;
	internal.itemsize= itemsize;
	internal.skip= skip;

	iter.internal= internal;
	iter.valid= (internal.ptr != internal.endptr);

	if(skip!=null && iter.valid && skip.skip(iter, internal.ptr))
		rna_iterator_array_next.next(iter);
}

public static PropCollectionNextFunc rna_iterator_array_next = new PropCollectionNextFunc() {
	public void next(CollectionPropertyIterator iter)
//public static void rna_iterator_array_next(CollectionPropertyIterator iter)
{
	ArrayIterator internal= (ArrayIterator)iter.internal;

	if(internal.skip!=null) {
		do {
			internal.ptr += internal.itemsize;
			iter.valid= (internal.ptr != internal.endptr);
		} while(iter.valid && internal.skip.skip(iter, internal.ptr));
	}
	else {
		internal.ptr += internal.itemsize;
		iter.valid= (internal.ptr != internal.endptr);
	}
}};

public static PropCollectionGetFunc rna_iterator_array_get = new PropCollectionGetFunc() {
	public PointerRNA get(CollectionPropertyIterator iter)
//public static Object rna_iterator_array_get(CollectionPropertyIterator iter)
{
	ArrayIterator internal= (ArrayIterator)iter.internal;

//	return internal.ptr;
	return (PointerRNA)internal.array[internal.ptr];
}};

//void *rna_iterator_array_dereference_get(CollectionPropertyIterator *iter)
//{
//	ArrayIterator *internal= iter.internal;
//
//	/* for ** arrays */
//	return *(void**)(internal.ptr);
//}

public static PropCollectionEndFunc rna_iterator_array_end = new PropCollectionEndFunc() {
	public void end(CollectionPropertyIterator iter)
//public static void rna_iterator_array_end(CollectionPropertyIterator iter)
{
	ArrayIterator internal= (ArrayIterator)iter.internal;
	
	if(internal.free_ptr!=null) {
//		MEM_freeN(internal.free_ptr);
		internal.free_ptr= null;
	}
//	MEM_freeN(iter.internal);
	iter.internal= null;
}};

/* RNA Path - Experiment */

static byte[] rna_path_token(byte[][] path, int[] path_p, byte[] fixedbuf, int fixedlen, int bracket, int[] buf_p)
{
//	const char *p;
	byte[] p;
	int p_p;
	byte[] buf;
//	int buf_p;
	byte quote= '\0';
	int i, j, len, escape;

	len= 0;

	if(bracket!=0) {
		/* get data between [], check escaping ] with \] */
//		if(**path == '[') (*path)++;
		if(path[0][path_p[0]] == '[') (path_p[0])++;
		else return null;

		p= path[0];
		p_p = path_p[0];

		/* 2 kinds of lookups now, quoted or unquoted */
		quote= p[p_p];

		if(quote != '"')
			quote= 0;

		if(quote==0) {
			while(p[p_p]!=0 && (p[p_p] != ']')) {
				len++;
				p_p++;
			}
		}
		else {
			escape= 0;
			/* skip the first quote */
			len++;
			p_p++;
			while(p[p_p]!=0 && (p[p_p] != quote || escape!=0)) {
				escape= (p[p_p] == '\\')?1:0;
				len++;
				p_p++;
			}
			
			/* skip the last quoted char to get the ']' */
			len++;
			p_p++;
		}

		if(p[p_p] != ']') return null;
	}
	else {
		/* get data until . or [ */
		p= path[0];
		p_p = path_p[0];

		while(p[p_p]!=0 && p[p_p] != '.' && p[p_p] != '[') {
			len++;
			p_p++;
		}
	}
	
	/* empty, return */
	if(len == 0)
		return null;
	
	/* try to use fixed buffer if possible */
	if(len+1 < fixedlen) {
		buf= fixedbuf;
		buf_p[0]= 0;
	}
	else {
		buf= new byte[len+1];
		buf_p[0]= 0;
	}

	/* copy string, taking into account escaped ] */
	if(bracket!=0) {
		for(p=path[0], p_p = path_p[0], i=0, j=0; i<len; i++, p_p++) {
			if(p[p_p] == '\\' && p[p_p+1] == quote);
			else buf[buf_p[0]+(j++)]= p[p_p];
		}

		buf[buf_p[0]+j]= 0;
	}
	else {
//		memcpy(buf, *path, sizeof(char)*len);
		System.arraycopy(path[0], path_p[0], buf, buf_p[0], len);
		buf[buf_p[0]+len]= '\0';
	}

	/* set path to start of next token */
	if(p[p_p] == ']') p_p++;
	if(p[p_p] == '.') p_p++;
	path[0]= p;
	path_p[0] = p_p;

	return buf;
}

static boolean rna_token_strip_quotes(byte[] token, int offset)
{
	if(token[offset+0]=='"') {
		int len = StringUtil.strlen(token,offset+0);
		if (len >= 2 && token[offset+len-1]=='"') {
			/* strip away "" */
			token[offset+len-1]= '\0';
			return true;
		}
	}
	return false;
}

/* Resolve the given RNA path to find the pointer+property indicated at the end of the path */
public static boolean RNA_path_resolve(PointerRNA ptr, byte[][] path, int[] path_p, PointerRNA[] r_ptr, PropertyRNA[] r_prop)
{
	return RNA_path_resolve_full(ptr, path, path_p, r_ptr, r_prop, null);
}

public static boolean RNA_path_resolve_full(PointerRNA ptr, byte[][] path, int[] path_p, PointerRNA[] r_ptr, PropertyRNA[] r_prop, int[] index)
{
	PropertyRNA prop;
	PointerRNA curptr=new PointerRNA();
	PointerRNA[] nextptr={new PointerRNA()};
	byte[] fixedbuf=new byte[256], token;
	int[] token_p={0};
	int type, intkey;
	
//	byte[] path = StringUtil.toCString(pathStr);
//	int[] path_p = {offset};

	prop= null;
	curptr= ptr;

	if(path[0]==null || path[0][path_p[0]]=='\0')
		return false;

	while(path[0][path_p[0]]!=0) {
		int use_id_prop = (path[0][path_p[0]]=='[') ? 1:0;
		/* custom property lookup ?
		 * C.object["someprop"]
		 */

		/* look up property name in current struct */
//		byte[][] _path = {path};
		token= rna_path_token(path, path_p, fixedbuf, fixedbuf.length, use_id_prop, token_p);
//		path = _path[0];

		if(token==null)
			return false;

		if(use_id_prop!=0) { /* look up property name in current struct */
			IDProperty group= RNA_struct_idprops(curptr, false);
			if(group!=null && rna_token_strip_quotes(token,token_p[0]))
				prop= (PropertyRNA)IdProp.IDP_GetPropertyFromGroup(group, token,token_p[0]+1);
		}
		else {
			prop= RNA_struct_find_property(curptr, token,token_p[0]);
		}

//		if(token != fixedbuf)
//			MEM_freeN(token);

		if(prop==null)
			return false;

		type= RNA_property_type(prop);

		/* now look up the value of this property if it is a pointer or
		 * collection, otherwise return the property rna so that the
		 * caller can read the value of the property itself */
		switch (type) {
		case RNATypes.PROP_POINTER:
			nextptr[0]= RNA_property_pointer_get(curptr, prop);

			if(nextptr[0].data!=null) {
				curptr= nextptr[0];
				prop= null; /* now we have a PointerRNA, the prop is our parent so forget it */
				if(index!=null) index[0]= -1;
			}
			else
				return false;

			break;
		case RNATypes.PROP_COLLECTION:
			if(path[0][path_p[0]]!=0) {
				if(path[0][path_p[0]] == '[') {
					/* resolve the lookup with [] brackets */
//					byte[][] _path2 = {path};
					token= rna_path_token(path, path_p, fixedbuf, fixedbuf.length, 1, token_p);
//					path = _path2[0];
	
					if(token==null)
						return false;
	
					/* check for "" to see if it is a string */
					if(rna_token_strip_quotes(token,token_p[0])) {
						RNA_property_collection_lookup_string(curptr, prop, token,token_p[0]+1, nextptr);
					}
					else {
						/* otherwise do int lookup */
//						intkey= atoi(token);
						intkey= Integer.parseInt(StringUtil.toJString(token,token_p[0]));
						if(intkey==0 && (token[token_p[0]+0] != '0' || token[token_p[0]+1] != '\0')) {
							return false; /* we can be sure the fixedbuf was used in this case */
						}
						RNA_property_collection_lookup_int(curptr, prop, intkey, nextptr);
					}

//					if(token != fixedbuf) {
//						MEM_freeN(token);
//					}
				}
				else {
					PointerRNA[] c_ptr = {null};
					
					/* ensure we quit on invalid values */
					nextptr[0].data = null;

					if(RNA_property_collection_type_get(curptr, prop, c_ptr)) {
						nextptr[0]= c_ptr[0];
					}
				}

				if(nextptr[0].data!=null) {
					curptr= nextptr[0];
					prop= null;  /* now we have a PointerRNA, the prop is our parent so forget it */
					if(index!=null) index[0]= -1;
				}
				else
					return false;
			}
			
			break;
		default:
			if (index==null)
				break;

			index[0]= -1;

			if (path[0][path_p[0]]!=0) {
				int[] index_arr=new int[RNATypes.RNA_MAX_ARRAY_DIMENSION];
				int[] len=new int[RNATypes.RNA_MAX_ARRAY_DIMENSION];
				/*const*/ int dim= RNA_property_array_dimension(curptr, prop, len);
				int i, temp_index;

				for(i=0; i<dim; i++) {
					temp_index= -1; 

					/* multi index resolve */
					if (path[0][path_p[0]]=='[') {
//						byte[][] _path2 = {path};
						token= rna_path_token(path, path_p, fixedbuf, fixedbuf.length, 1, token_p);
//						path = _path2[0];
	
						if(token==null) {
							/* invalid syntax blah[] */
							return false;
						}
						/* check for "" to see if it is a string */
						else if(rna_token_strip_quotes(token,token_p[0])) {
//							temp_index= RNA_property_array_item_index(prop, *(token+1));
							temp_index= RNA_property_array_item_index(prop, (char)token[token_p[0]+1]);
						}
						else {
							/* otherwise do int lookup */
//							temp_index= atoi(token);
							temp_index= Integer.parseInt(StringUtil.toJString(token,token_p[0]));

							if(temp_index==0 && (token[token_p[0]+0] != '0' || token[token_p[0]+1] != '\0')) {
//								if(token != fixedbuf) {
//									MEM_freeN(token);
//								}

								return false;
							}
						}
					}
					else if(dim==1) {
						/* location.x || scale.X, single dimension arrays only */
//						byte[][] _path2 = {path};
						token= rna_path_token(path, path_p, fixedbuf, fixedbuf.length, 0, token_p);
//						path = _path2[0];
						if(token==null) {
							/* invalid syntax blah.. */
							return false;
						}
						temp_index= RNA_property_array_item_index(prop, (char)token[token_p[0]]);
					}
	
//					if(token != fixedbuf) {
//						MEM_freeN(token);
//					}
					
					/* out of range */
					if(temp_index < 0 || temp_index >= len[i])
						return false;

					index_arr[i]= temp_index;
					/* end multi index resolve */
				}

				/* arrays always contain numbers so further values are not valid */
				if(path[0][path_p[0]]!=0) {
					return false;
				}
				else {
					int totdim= 1;
					int flat_index= 0;

					for(i=dim-1; i>=0; i--) {
						flat_index += index_arr[i] * totdim;
						totdim *= len[i];
					}

					index[0]= flat_index;
				}
			}
		}
	}

	r_ptr[0]= curptr;
	r_prop[0]= prop;

	return true;
}

//char *RNA_path_append(const char *path, PointerRNA *ptr, PropertyRNA *prop, int intkey, const char *strkey)
//{
//	DynStr *dynstr;
//	const char *s;
//	char appendstr[128], *result;
//
//	dynstr= BLI_dynstr_new();
//
//	/* add .identifier */
//	if(path) {
//		BLI_dynstr_append(dynstr, (char*)path);
//		if(*path)
//			BLI_dynstr_append(dynstr, ".");
//	}
//
//	BLI_dynstr_append(dynstr, (char*)RNA_property_identifier(prop));
//
//	if(RNA_property_type(prop) == PROP_COLLECTION) {
//		/* add ["strkey"] or [intkey] */
//		BLI_dynstr_append(dynstr, "[");
//
//		if(strkey) {
//			BLI_dynstr_append(dynstr, "\"");
//			for(s=strkey; *s; s++) {
//				if(*s == '[') {
//					appendstr[0]= '\\';
//					appendstr[1]= *s;
//					appendstr[2]= 0;
//				}
//				else {
//					appendstr[0]= *s;
//					appendstr[1]= 0;
//				}
//				BLI_dynstr_append(dynstr, appendstr);
//			}
//			BLI_dynstr_append(dynstr, "\"");
//		}
//		else {
//			sprintf(appendstr, "%d", intkey);
//			BLI_dynstr_append(dynstr, appendstr);
//		}
//
//		BLI_dynstr_append(dynstr, "]");
//	}
//
//	result= BLI_dynstr_get_cstring(dynstr);
//	BLI_dynstr_free(dynstr);
//
//	return result;
//}
//
//char *RNA_path_back(const char *path)
//{
//	char fixedbuf[256];
//	const char *previous, *current;
//	char *result, *token;
//	int i;
//
//	if(!path)
//		return NULL;
//
//	previous= NULL;
//	current= path;
//
//	/* parse token by token until the end, then we back up to the previous
//	 * position and strip of the next token to get the path one step back */
//	while(*current) {
//		token= rna_path_token(&current, fixedbuf, sizeof(fixedbuf), 0);
//
//		if(!token)
//			return NULL;
//		if(token != fixedbuf)
//			MEM_freeN(token);
//
//		/* in case of collection we also need to strip off [] */
//		token= rna_path_token(&current, fixedbuf, sizeof(fixedbuf), 1);
//		if(token && token != fixedbuf)
//			MEM_freeN(token);
//
//		if(!*current)
//			break;
//
//		previous= current;
//	}
//
//	if(!previous)
//		return NULL;
//
//	/* copy and strip off last token */
//	i= previous - path;
//	result= BLI_strdup(path);
//
//	if(i > 0 && result[i-1] == '.') i--;
//	result[i]= 0;
//
//	return result;
//}
//
//char *RNA_path_from_ID_to_property(PointerRNA *ptr, PropertyRNA *prop)
//{
//	char *ptrpath=NULL, *path;
//	const char *propname;
//
//	if(!ptr.id.data || !ptr.data || !prop)
//		return NULL;
//
//	if(!RNA_struct_is_ID(ptr.type)) {
//		if(ptr.type.path) {
//			/* if type has a path to some ID, use it */
//			ptrpath= ptr.type.path(ptr);
//		}
//		else if(ptr.type.nested) {
//			PointerRNA parentptr;
//			PropertyRNA *userprop;
//
//			/* find the property in the struct we're nested in that references this struct, and
//			 * use its identifier as the first part of the path used...
//			 */
//			RNA_pointer_create(ptr.id.data, ptr.type.nested, ptr.data, &parentptr);
//			userprop= RNA_struct_find_nested(&parentptr, ptr.type);
//
//			if(userprop)
//				ptrpath= BLI_strdup(RNA_property_identifier(userprop));
//			else
//				return NULL; // can't do anything about this case yet...
//		}
//		else
//			return NULL;
//	}
//
//	propname= RNA_property_identifier(prop);
//
//	if(ptrpath) {
//		path= BLI_sprintfN("%s.%s", ptrpath, propname);
//		MEM_freeN(ptrpath);
//	}
//	else
//		path= BLI_strdup(propname);
//
//	return path;
//}

/* Quick name based property access */

public static boolean RNA_boolean_get(Object ptr, String name)
{
    // TMP
    Object result = ((PointerRNA)ptr).attr.get(name);
    return result!=null?(Boolean)result:false;

//	PropertyRNA *prop= RNA_struct_find_property(ptr, name);
//
//	if(prop) {
//		return RNA_property_boolean_get(ptr, prop);
//	}
//	else {
//		printf("RNA_boolean_get: %s.%s not found.\n", ptr.type.identifier, name);
//		return false;
//	}
}

public static void RNA_boolean_set(Object ptr, String name, boolean value)
{
    // TMP
    ((PointerRNA)ptr).attr.put(name, value);

//	PropertyRNA *prop= RNA_struct_find_property(ptr, name);
//
//	if(prop)
//		RNA_property_boolean_set(ptr, prop, value);
//	else
//		printf("RNA_boolean_set: %s.%s not found.\n", ptr.type.identifier, name);
}

public static void RNA_boolean_get_array(Object ptr, String name, boolean[] values)
{
    // TMP
    Object result = ((PointerRNA)ptr).attr.get(name);
    if (result!=null) {
        System.arraycopy((boolean[])result, 0, values, 0, ((boolean[])result).length);
    }

//	PropertyRNA *prop= RNA_struct_find_property(ptr, name);
//
//	if(prop)
//		RNA_property_boolean_get_array(ptr, prop, values);
//	else
//		printf("RNA_boolean_get_array: %s.%s not found.\n", ptr.type.identifier, name);
}

public static void RNA_boolean_set_array(Object ptr, String name, boolean[] values)
{
    // TMP
    ((PointerRNA)ptr).attr.put(name, values);

//	PropertyRNA *prop= RNA_struct_find_property(ptr, name);
//
//	if(prop)
//		RNA_property_boolean_set_array(ptr, prop, values);
//	else
//		printf("RNA_boolean_set_array: %s.%s not found.\n", ptr.type.identifier, name);
}

public static int RNA_int_get(Object _ptr, String name)
{
    // TMP
    Object result = ((PointerRNA)_ptr).attr.get(name);
    return result!=null?(Integer)result:0;

//	PointerRNA ptr = (PointerRNA) _ptr;
//	PropertyRNA prop= RNA_struct_find_property(ptr, StringUtil.toCString(name),0);
//
//	if(prop!=null) {
//		return RNA_property_int_get(ptr, prop);
//	}
//	else {
//		System.out.printf("RNA_int_get: %s.%s not found.\n", ptr.type.identifier, name);
//		return 0;
//	}
}

public static void RNA_int_set(Object _ptr, String name, int value)
{
    // TMP
    ((PointerRNA)_ptr).attr.put(name, value);

//	PointerRNA ptr = (PointerRNA) _ptr;
//	PropertyRNA prop= RNA_struct_find_property(ptr, StringUtil.toCString(name),0);
//
//	if(prop!=null)
//		RNA_property_int_set(ptr, prop, value);
//	else
//		System.out.printf("RNA_int_set: %s.%s not found.\n", ptr.type.identifier, name);
}

//void RNA_int_get_array(PointerRNA *ptr, const char *name, int *values)
//{
//	PropertyRNA *prop= RNA_struct_find_property(ptr, name);
//
//	if(prop)
//		RNA_property_int_get_array(ptr, prop, values);
//	else
//		printf("RNA_int_get_array: %s.%s not found.\n", ptr.type.identifier, name);
//}
//
//void RNA_int_set_array(PointerRNA *ptr, const char *name, const int *values)
//{
//	PropertyRNA *prop= RNA_struct_find_property(ptr, name);
//
//	if(prop)
//		RNA_property_int_set_array(ptr, prop, values);
//	else
//		printf("RNA_int_set_array: %s.%s not found.\n", ptr.type.identifier, name);
//}

public static float RNA_float_get(Object ptr, String name)
{
    // TMP
    Object result = ((PointerRNA)ptr).attr.get(name);
    return result!=null?(Float)result:0;

//	PropertyRNA *prop= RNA_struct_find_property(ptr, name);
//
//	if(prop) {
//		return RNA_property_float_get(ptr, prop);
//	}
//	else {
//		printf("RNA_float_get: %s.%s not found.\n", ptr.type.identifier, name);
//		return 0;
//	}
}

public static void RNA_float_set(Object ptr, String name, float value)
{
    // TMP
    ((PointerRNA)ptr).attr.put(name, value);

//	PropertyRNA *prop= RNA_struct_find_property(ptr, name);
//
//	if(prop)
//		RNA_property_float_set(ptr, prop, value);
//	else
//		printf("RNA_float_set: %s.%s not found.\n", ptr.type.identifier, name);
}

public static void RNA_float_get_array(Object ptr, String name, float[] values)
{
    // TMP
    Object result = ((PointerRNA)ptr).attr.get(name);
    if (result!=null) {
        System.arraycopy((float[])result, 0, values, 0, ((float[])result).length);
    }

//	PropertyRNA *prop= RNA_struct_find_property(ptr, name);
//
//	if(prop)
//		RNA_property_float_get_array(ptr, prop, values);
//	else
//		printf("RNA_float_get_array: %s.%s not found.\n", ptr.type.identifier, name);
}

public static void RNA_float_set_array(Object ptr, String name, float[] values)
{
    // TMP
    ((PointerRNA)ptr).attr.put(name, values);

//	PropertyRNA *prop= RNA_struct_find_property(ptr, name);
//
//	if(prop)
//		RNA_property_float_set_array(ptr, prop, values);
//	else
//		printf("RNA_float_set_array: %s.%s not found.\n", ptr.type.identifier, name);
}

public static int RNA_enum_get(Object ptr, String name)
{
    // TMP
    Object result = ((PointerRNA)ptr).attr.get(name);
    return result!=null?(Integer)result:0;

//	PropertyRNA *prop= RNA_struct_find_property(ptr, name);
//
//	if(prop) {
//		return RNA_property_enum_get(ptr, prop);
//	}
//	else {
//		printf("RNA_enum_get: %s.%s not found.\n", ptr.type.identifier, name);
//		return 0;
//	}
}

public static void RNA_enum_set(Object ptr, String name, int value)
{
    // TMP
    ((PointerRNA)ptr).attr.put(name, value);

//	PropertyRNA *prop= RNA_struct_find_property(ptr, name);
//
//	if(prop)
//		RNA_property_enum_set(ptr, prop, value);
//	else
//		printf("RNA_enum_set: %s.%s not found.\n", ptr.type.identifier, name);
}

//int RNA_enum_is_equal(bContext *C, PointerRNA *ptr, const char *name, const char *enumname)
//{
//	PropertyRNA *prop= RNA_struct_find_property(ptr, name);
//	EnumPropertyItem *item;
//	int free;
//
//	if(prop) {
//		RNA_property_enum_items(C, ptr, prop, &item, NULL, &free);
//
//		for(; item.identifier; item++)
//			if(strcmp(item.identifier, enumname) == 0)
//				return (item.value == RNA_property_enum_get(ptr, prop));
//
//		if(free)
//			MEM_freeN(item);
//
//		printf("RNA_enum_is_equal: %s.%s item %s not found.\n", ptr.type.identifier, name, enumname);
//		return 0;
//	}
//	else {
//		printf("RNA_enum_is_equal: %s.%s not found.\n", ptr.type.identifier, name);
//		return 0;
//	}
//}

public static boolean RNA_enum_value_from_id(EnumPropertyItem[] item, int offset, String identifier, int[] value)
{
	for( ; item[offset].identifier!=null; offset++) {
//		if(strcmp(item[offset].identifier, identifier)==0) {
		if(item[offset].identifier.equals(identifier)) {
			value[0]= item[offset].value;
			return true;
		}
	}

	return false;
}

//int	RNA_enum_id_from_value(EnumPropertyItem *item, int value, const char **identifier)
//{
//	for( ; item.identifier; item++) {
//		if(item.value==value) {
//			*identifier= item.identifier;
//			return 1;
//		}
//	}
//
//	return 0;
//}



public static String RNA_string_get(Object ptr, String name)
{
    // TMP
    Object result = ((PointerRNA)ptr).attr.get(name);
    return result!=null?(String)result:null;

//	PropertyRNA *prop= RNA_struct_find_property(ptr, name);
//
//	if(prop)
//		RNA_property_string_get(ptr, prop, value);
//	else
//		printf("RNA_string_get: %s.%s not found.\n", ptr.type.identifier, name);
//    return null;
}

//char *RNA_string_get_alloc(PointerRNA *ptr, const char *name, char *fixedbuf, int fixedlen)
//{
//	PropertyRNA *prop= RNA_struct_find_property(ptr, name);
//
//	if(prop) {
//		return RNA_property_string_get_alloc(ptr, prop, fixedbuf, fixedlen);
//	}
//	else {
//		printf("RNA_string_get_alloc: %s.%s not found.\n", ptr.type.identifier, name);
//		return 0;
//	}
//}
//
//int RNA_string_length(PointerRNA *ptr, const char *name)
//{
//	PropertyRNA *prop= RNA_struct_find_property(ptr, name);
//
//	if(prop) {
//		return RNA_property_string_length(ptr, prop);
//	}
//	else {
//		printf("RNA_string_length: %s.%s not found.\n", ptr.type.identifier, name);
//		return 0;
//	}
//}

public static void RNA_string_set(Object ptr, String name, String value)
{
    // TMP
    ((PointerRNA)ptr).attr.put(name, value);

//	PropertyRNA *prop= RNA_struct_find_property(ptr, name);
//
//	if(prop)
//		RNA_property_string_set(ptr, prop, value);
//	else
//		printf("RNA_string_set: %s.%s not found.\n", ptr.type.identifier, name);
}

//PointerRNA RNA_pointer_get(PointerRNA *ptr, const char *name)
//{
//	PropertyRNA *prop= RNA_struct_find_property(ptr, name);
//
//	if(prop) {
//		return RNA_property_pointer_get(ptr, prop);
//	}
//	else {
//		PointerRNA result;
//
//		printf("RNA_pointer_get: %s.%s not found.\n", ptr.type.identifier, name);
//
//		memset(&result, 0, sizeof(result));
//		return result;
//	}
//}
//
//void RNA_pointer_set(PointerRNA *ptr, const char *name, PointerRNA ptr_value)
//{
//	PropertyRNA *prop= RNA_struct_find_property(ptr, name);
//
//	if(prop) {
//		RNA_property_pointer_set(ptr, prop, ptr_value);
//	}
//	else {
//		printf("RNA_pointer_set: %s.%s not found.\n", ptr.type.identifier, name);
//	}
//}
//
//void RNA_pointer_add(PointerRNA *ptr, const char *name)
//{
//	PropertyRNA *prop= RNA_struct_find_property(ptr, name);
//
//	if(prop)
//		RNA_property_pointer_add(ptr, prop);
//	else
//		printf("RNA_pointer_set: %s.%s not found.\n", ptr.type.identifier, name);
//}
//
//void RNA_collection_begin(PointerRNA *ptr, const char *name, CollectionPropertyIterator *iter)
//{
//	PropertyRNA *prop= RNA_struct_find_property(ptr, name);
//
//	if(prop)
//		RNA_property_collection_begin(ptr, prop, iter);
//	else
//		printf("RNA_collection_begin: %s.%s not found.\n", ptr.type.identifier, name);
//}
//
//void RNA_collection_add(PointerRNA *ptr, const char *name, PointerRNA *r_value)
//{
//	PropertyRNA *prop= RNA_struct_find_property(ptr, name);
//
//	if(prop)
//		RNA_property_collection_add(ptr, prop, r_value);
//	else
//		printf("RNA_collection_add: %s.%s not found.\n", ptr.type.identifier, name);
//}
//
//void RNA_collection_clear(PointerRNA *ptr, const char *name)
//{
//	PropertyRNA *prop= RNA_struct_find_property(ptr, name);
//
//	if(prop)
//		RNA_property_collection_clear(ptr, prop);
//	else
//		printf("RNA_collection_clear: %s.%s not found.\n", ptr.type.identifier, name);
//}
//
//int RNA_collection_length(PointerRNA *ptr, const char *name)
//{
//	PropertyRNA *prop= RNA_struct_find_property(ptr, name);
//
//	if(prop) {
//		return RNA_property_collection_length(ptr, prop);
//	}
//	else {
//		printf("RNA_collection_length: %s.%s not found.\n", ptr.type.identifier, name);
//		return 0;
//	}
//}

public static boolean RNA_property_is_set(Object ptr, String name)
{
    // TMP
    Object result = ((PointerRNA)ptr).attr.get(name);
    return result!=null;

//	PropertyRNA *prop= RNA_struct_find_property(ptr, name);
//
//	if(prop) {
//		return (rna_idproperty_find(ptr, name) != NULL);
//	}
//	else {
//		printf("RNA_property_is_set: %s.%s not found.\n", ptr.type.identifier, name);
//		return 0;
//	}
}

///* string representation of a property, python
// * compatible but can be used for display too*/
//char *RNA_pointer_as_string(PointerRNA *ptr)
//{
//	DynStr *dynstr= BLI_dynstr_new();
//	char *cstring;
//
//	const char *propname;
//	int first_time = 1;
//
//	BLI_dynstr_append(dynstr, "{");
//
//	RNA_STRUCT_BEGIN(ptr, prop) {
//		propname = RNA_property_identifier(prop);
//
//		if(strcmp(propname, "rna_type")==0)
//			continue;
//
//		if(first_time==0)
//			BLI_dynstr_append(dynstr, ", ");
//		first_time= 0;
//
//		cstring = RNA_property_as_string(ptr, prop);
//		BLI_dynstr_appendf(dynstr, "\"%s\":%s", propname, cstring);
//		MEM_freeN(cstring);
//	}
//	RNA_STRUCT_END;
//
//	BLI_dynstr_append(dynstr, "}");
//
//
//	cstring = BLI_dynstr_get_cstring(dynstr);
//	BLI_dynstr_free(dynstr);
//	return cstring;
//}
//
//char *RNA_property_as_string(PointerRNA *ptr, PropertyRNA *prop)
//{
//	int type = RNA_property_type(prop);
//	int len = RNA_property_array_length(prop);
//	int i;
//
//	DynStr *dynstr= BLI_dynstr_new();
//	char *cstring;
//
//
//	/* see if we can coorce into a python type - PropertyType */
//	switch (type) {
//	case PROP_BOOLEAN:
//		if(len==0) {
//			BLI_dynstr_append(dynstr, RNA_property_boolean_get(ptr, prop) ? "True" : "False");
//		}
//		else {
//			BLI_dynstr_append(dynstr, "(");
//			for(i=0; i<len; i++) {
//				BLI_dynstr_appendf(dynstr, i?", %s":"%s", RNA_property_boolean_get_index(ptr, prop, i) ? "True" : "False");
//			}
//			if(len==1)
//				BLI_dynstr_append(dynstr, ","); /* otherwise python wont see it as a tuple */
//			BLI_dynstr_append(dynstr, ")");
//		}
//		break;
//	case PROP_INT:
//		if(len==0) {
//			BLI_dynstr_appendf(dynstr, "%d", RNA_property_int_get(ptr, prop));
//		}
//		else {
//			BLI_dynstr_append(dynstr, "(");
//			for(i=0; i<len; i++) {
//				BLI_dynstr_appendf(dynstr, i?", %d":"%d", RNA_property_int_get_index(ptr, prop, i));
//			}
//			if(len==1)
//				BLI_dynstr_append(dynstr, ","); /* otherwise python wont see it as a tuple */
//			BLI_dynstr_append(dynstr, ")");
//		}
//		break;
//	case PROP_FLOAT:
//		if(len==0) {
//			BLI_dynstr_appendf(dynstr, "%g", RNA_property_float_get(ptr, prop));
//		}
//		else {
//			BLI_dynstr_append(dynstr, "(");
//			for(i=0; i<len; i++) {
//				BLI_dynstr_appendf(dynstr, i?", %g":"%g", RNA_property_float_get_index(ptr, prop, i));
//			}
//			if(len==1)
//				BLI_dynstr_append(dynstr, ","); /* otherwise python wont see it as a tuple */
//			BLI_dynstr_append(dynstr, ")");
//		}
//		break;
//	case PROP_STRING:
//	{
//		/* string arrays dont exist */
//		char *buf;
//		buf = RNA_property_string_get_alloc(ptr, prop, NULL, -1);
//		BLI_dynstr_appendf(dynstr, "\"%s\"", buf);
//		MEM_freeN(buf);
//		break;
//	}
//	case PROP_ENUM:
//	{
//		/* string arrays dont exist */
//		const char *identifier;
//		int val = RNA_property_enum_get(ptr, prop);
//
//		if(RNA_property_enum_identifier(NULL, ptr, prop, val, &identifier)) {
//			BLI_dynstr_appendf(dynstr, "'%s'", identifier);
//		}
//		else {
//			BLI_dynstr_appendf(dynstr, "'<UNKNOWN ENUM>'", identifier);
//		}
//		break;
//	}
//	case PROP_POINTER:
//	{
//		BLI_dynstr_append(dynstr, "'<POINTER>'"); /* TODO */
//		break;
//	}
//	case PROP_COLLECTION:
//	{
//		int first_time = 1;
//		CollectionPropertyIterator collect_iter;
//		BLI_dynstr_append(dynstr, "[");
//
//		for(RNA_property_collection_begin(ptr, prop, &collect_iter); collect_iter.valid; RNA_property_collection_next(&collect_iter)) {
//			PointerRNA itemptr= collect_iter.ptr;
//
//			if(first_time==0)
//				BLI_dynstr_append(dynstr, ", ");
//			first_time= 0;
//
//			/* now get every prop of the collection */
//			cstring= RNA_pointer_as_string(&itemptr);
//			BLI_dynstr_append(dynstr, cstring);
//			MEM_freeN(cstring);
//		}
//
//		RNA_property_collection_end(&collect_iter);
//		BLI_dynstr_append(dynstr, "]");
//		break;
//	}
//	default:
//		BLI_dynstr_append(dynstr, "'<UNKNOWN TYPE>'"); /* TODO */
//		break;
//	}
//
//	cstring = BLI_dynstr_get_cstring(dynstr);
//	BLI_dynstr_free(dynstr);
//	return cstring;
//}

/* Function */

public static String RNA_function_identifier(FunctionRNA func)
{
	return func.identifier;
}

//PropertyRNA *RNA_function_return(FunctionRNA *func)
//{
//	return func.ret;
//}
//
//const char *RNA_function_ui_description(FunctionRNA *func)
//{
//	return func.description;
//}

public static int RNA_function_flag(FunctionRNA func)
{
	return func.flag;
}

//PropertyRNA *RNA_function_get_parameter(PointerRNA *ptr, FunctionRNA *func, int index)
//{
//	PropertyRNA *parm;
//	int i;
//
//	parm= func.cont.properties.first;
//	for(i= 0; parm; parm= parm.next, i++)
//		if(i==index)
//			return parm;
//
//	return NULL;
//}
//
//PropertyRNA *RNA_function_find_parameter(PointerRNA *ptr, FunctionRNA *func, const char *identifier)
//{
//	PropertyRNA *parm;
//
//	parm= func.cont.properties.first;
//	for(; parm; parm= parm.next)
//		if(strcmp(parm.identifier, identifier)==0)
//			return parm;
//
//	return NULL;
//}

public static ListBase RNA_function_defined_parameters(FunctionRNA func)
{
	return func.cont.properties;
}

/* Utility */

public static ParameterList RNA_parameter_list_create(ParameterList parms, PointerRNA ptr, FunctionRNA func)
{
	PropertyRNA parm;
	int tot= 0;

	for(parm= (PropertyRNA)func.cont.properties.first; parm!=null; parm= (PropertyRNA)parm.next)
//		tot+= rna_parameter_size(parm);
		tot++;

//	parms.data= MEM_callocN(tot, "RNA_parameter_list_create");
	parms.data= new Object[tot];
	parms.func= func;
	parms.tot= tot;

	return parms;
}

//void RNA_parameter_list_free(ParameterList *parms)
//{
//	PropertyRNA *parm;
//	int tot;
//
//	parm= parms.func.cont.properties.first;
//	for(tot= 0; parm; parm= parm.next) {
//		if(parm.type == PROP_COLLECTION)
//			BLI_freelistN((ListBase*)((char*)parms.data+tot));
//
//		tot+= rna_parameter_size(parm);
//	}
//
//	MEM_freeN(parms.data);
//	parms.data= NULL;
//
//	parms.func= NULL;
//}
//
//int  RNA_parameter_list_size(ParameterList *parms)
//{
//	return parms.tot;
//}

public static void RNA_parameter_list_begin(ParameterList parms, ParameterIterator iter)
{
//	PropertyType ptype;

//	RNA_pointer_create(null, &RNA_Function, parms.func, &iter.funcptr);
//	RNA_pointer_create(null, new FunctionRNA(), parms.func, iter.funcptr);
	RNA_pointer_create(null, new StructRNA(), parms.func, iter.funcptr);

	iter.parms= parms;
	iter.parm= (PropertyRNA)parms.func.cont.properties.first;
	iter.valid= iter.parm != null;
	iter.offset= 0;

	if(iter.valid) {
		iter.size= RnaDefine.rna_parameter_size(iter.parm);
//		iter.data= (((char*)iter.parms.data)+iter.offset);
//		ptype= RNA_property_type(iter.parm);
	}
}

public static void RNA_parameter_list_next(ParameterIterator iter)
{
//	PropertyType ptype;

	iter.offset+= iter.size;
	iter.parm= (PropertyRNA)iter.parm.next;
	iter.valid= iter.parm != null;

	if(iter.valid) {
		iter.size= RnaDefine.rna_parameter_size(iter.parm);
//		iter.data= (((char*)iter.parms.data)+iter.offset);
//		ptype= RNA_property_type(iter.parm);
	}
}

public static void RNA_parameter_list_end(ParameterIterator iter)
{
	/* nothing to do */
}

//void RNA_parameter_get(ParameterList *parms, PropertyRNA *parm, void **value)
//{
//	ParameterIterator iter;
//
//	RNA_parameter_list_begin(parms, &iter);
//
//	for(; iter.valid; RNA_parameter_list_next(&iter))
//		if(iter.parm==parm)
//			break;
//
//	if(iter.valid)
//		*value= iter.data;
//	else
//		*value= NULL;
//
//	RNA_parameter_list_end(&iter);
//}
//
//void RNA_parameter_get_lookup(ParameterList *parms, const char *identifier, void **value)
//{
//	PropertyRNA *parm;
//
//	parm= parms.func.cont.properties.first;
//	for(; parm; parm= parm.next)
//		if(strcmp(RNA_property_identifier(parm), identifier)==0)
//			break;
//
//	if(parm)
//		RNA_parameter_get(parms, parm, value);
//}

public static void RNA_parameter_set(ParameterList parms, PropertyRNA parm, Object value)
{
	ParameterIterator iter = new ParameterIterator();

	RNA_parameter_list_begin(parms, iter);

	for(; iter.valid; RNA_parameter_list_next(iter))
		if(iter.parm==parm)
			break;

	if(iter.valid)
//		memcpy(iter.data, value, iter.size);
		iter.data(value);

	RNA_parameter_list_end(iter);
}

public static void RNA_parameter_set_lookup(ParameterList parms, String identifier, Object value)
{
	PropertyRNA parm;

	parm= (PropertyRNA)parms.func.cont.properties.first;
	for(; parm!=null; parm= (PropertyRNA)parm.next)
//		if(strcmp(RNA_property_identifier(parm), identifier)==0)
		if(RNA_property_identifier(parm).equals(identifier))
			break;

	if(parm!=null)
		RNA_parameter_set(parms, parm, value);
}

//int RNA_function_call(bContext *C, ReportList *reports, PointerRNA *ptr, FunctionRNA *func, ParameterList *parms)
//{
//	if(func.call) {
//		func.call(C, reports, ptr, parms);
//
//		return 0;
//	}
//
//	return -1;
//}
//
//int RNA_function_call_lookup(bContext *C, ReportList *reports, PointerRNA *ptr, const char *identifier, ParameterList *parms)
//{
//	FunctionRNA *func;
//
//	func= RNA_struct_find_function(ptr, identifier);
//
//	if(func)
//		return RNA_function_call(C, reports, ptr, func, parms);
//
//	return -1;
//}
//
//int RNA_function_call_direct(bContext *C, ReportList *reports, PointerRNA *ptr, FunctionRNA *func, const char *format, ...)
//{
//	va_list args;
//	int ret;
//
//	va_start(args, format);
//
//	ret= RNA_function_call_direct_va(C, reports, ptr, func, format, args);
//
//	va_end(args);
//
//	return ret;
//}
//
//int RNA_function_call_direct_lookup(bContext *C, ReportList *reports, PointerRNA *ptr, const char *identifier, const char *format, ...)
//{
//	FunctionRNA *func;
//
//	func= RNA_struct_find_function(ptr, identifier);
//
//	if(func) {
//		va_list args;
//		int ret;
//
//		va_start(args, format);
//
//		ret= RNA_function_call_direct_va(C, reports, ptr, func, format, args);
//
//		va_end(args);
//
//		return ret;
//	}
//
//	return -1;
//}
//
//static int rna_function_format_array_length(const char *format, int ofs, int flen)
//{
//	char lenbuf[16];
//	int idx= 0;
//
//	if (format[ofs++]=='[')
//		for (; ofs<flen && format[ofs]!=']' && idx<sizeof(*lenbuf)-1; idx++, ofs++)
//			lenbuf[idx]= format[ofs];
//
//	if (ofs<flen && format[ofs++]==']') {
//		/* XXX put better error reporting for ofs>=flen or idx over lenbuf capacity */
//		lenbuf[idx]= '\0';
//		return atoi(lenbuf);
//	}
//
//	return 0;
//}
//
//static int rna_function_parameter_parse(PointerRNA *ptr, PropertyRNA *prop, PropertyType type, char ftype, int len, void *dest, void *src, StructRNA *srna, const char *tid, const char *fid, const char *pid)
//{
//	/* ptr is always a function pointer, prop always a parameter */
//
//	switch (type) {
//	case PROP_BOOLEAN:
//		{
//			if (ftype!='b') {
//				fprintf(stderr, "%s.%s: wrong type for parameter %s, a boolean was expected\n", tid, fid, pid);
//				return -1;
//			}
//
//			if (len==0)
//				*((int*)dest)= *((int*)src);
//			else
//				memcpy(dest, src, len*sizeof(int));
//
//			break;
//		}
//	case PROP_INT:
//		{
//			if (ftype!='i') {
//				fprintf(stderr, "%s.%s: wrong type for parameter %s, an integer was expected\n", tid, fid, pid);
//				return -1;
//			}
//
//			if (len==0)
//				*((int*)dest)= *((int*)src);
//			else
//				memcpy(dest, src, len*sizeof(int));
//
//			break;
//		}
//	case PROP_FLOAT:
//		{
//			if (ftype!='f') {
//				fprintf(stderr, "%s.%s: wrong type for parameter %s, a float was expected\n", tid, fid, pid);
//				return -1;
//			}
//
//			if (len==0)
//				*((float*)dest)= *((float*)src);
//			else
//				memcpy(dest, src, len*sizeof(float));
//
//			break;
//		}
//	case PROP_STRING:
//		{
//			if (ftype!='s') {
//				fprintf(stderr, "%s.%s: wrong type for parameter %s, a string was expected\n", tid, fid, pid);
//				return -1;
//			}
//
//			*((char**)dest)= *((char**)src);
//
//			break;
//		}
//	case PROP_ENUM:
//		{
//			if (ftype!='e') {
//				fprintf(stderr, "%s.%s: wrong type for parameter %s, an enum was expected\n", tid, fid, pid);
//				return -1;
//			}
//
//			*((int*)dest)= *((int*)src);
//
//			break;
//		}
//	case PROP_POINTER:
//		{
//			StructRNA *ptype;
//
//			if (ftype!='O') {
//				fprintf(stderr, "%s.%s: wrong type for parameter %s, an object was expected\n", tid, fid, pid);
//				return -1;
//			}
//
//			ptype= RNA_property_pointer_type(ptr, prop);
//
//			if(prop.flag & PROP_RNAPTR) {
//				*((PointerRNA*)dest)= *((PointerRNA*)src);
//				break;
// 			}
//
//			if (ptype!=srna && !RNA_struct_is_a(srna, ptype)) {
//				fprintf(stderr, "%s.%s: wrong type for parameter %s, an object of type %s was expected, passed an object of type %s\n", tid, fid, pid, RNA_struct_identifier(ptype), RNA_struct_identifier(srna));
//				return -1;
//			}
//
//			*((void**)dest)= *((void**)src);
//
//			break;
//		}
//	case PROP_COLLECTION:
//		{
//			StructRNA *ptype;
//			ListBase *lb, *clb;
//			Link *link;
//			CollectionPointerLink *clink;
//
//			if (ftype!='C') {
//				fprintf(stderr, "%s.%s: wrong type for parameter %s, a collection was expected\n", tid, fid, pid);
//				return -1;
//			}
//
//			lb= (ListBase *)src;
//			clb= (ListBase *)dest;
//			ptype= RNA_property_pointer_type(ptr, prop);
//
//			if (ptype!=srna && !RNA_struct_is_a(srna, ptype)) {
//				fprintf(stderr, "%s.%s: wrong type for parameter %s, a collection of objects of type %s was expected, passed a collection of objects of type %s\n", tid, fid, pid, RNA_struct_identifier(ptype), RNA_struct_identifier(srna));
//				return -1;
//			}
//
//			for (link= lb.first; link; link= link.next) {
//				clink= MEM_callocN(sizeof(CollectionPointerLink), "CCollectionPointerLink");
//				RNA_pointer_create(NULL, srna, link, &clink.ptr);
//				BLI_addtail(clb, clink);
//			}
//
//			break;
//		}
//	default:
//		{
//			if (len==0)
//				fprintf(stderr, "%s.%s: unknown type for parameter %s\n", tid, fid, pid);
//			else
//				fprintf(stderr, "%s.%s: unknown array type for parameter %s\n", tid, fid, pid);
//
//			return -1;
//		}
//	}
//
//	return 0;
//}
//
//int RNA_function_call_direct_va(bContext *C, ReportList *reports, PointerRNA *ptr, FunctionRNA *func, const char *format, va_list args)
//{
//	PointerRNA funcptr;
//	ParameterList parms;
//	ParameterIterator iter;
//	PropertyRNA *pret, *parm;
//	PropertyType type;
//	int i, ofs, flen, flag, len, alen, err= 0;
//	const char *tid, *fid, *pid=NULL;
//	char ftype;
//	void **retdata=NULL;
//
//	RNA_pointer_create(NULL, &RNA_Function, func, &funcptr);
//
//	tid= RNA_struct_identifier(ptr.type);
//	fid= RNA_function_identifier(func);
//	pret= RNA_function_return(func);
//	flen= strlen(format);
//
//	RNA_parameter_list_create(&parms, ptr, func);
//	RNA_parameter_list_begin(&parms, &iter);
//
//	for(i= 0, ofs= 0; iter.valid; RNA_parameter_list_next(&iter), i++) {
//		parm= iter.parm;
//
//		if(parm==pret) {
//			retdata= iter.data;
//			continue;
//		}
//
//		pid= RNA_property_identifier(parm);
//		flag= RNA_property_flag(parm);
//
//		if (ofs>=flen || format[ofs]=='N') {
//			if (flag & PROP_REQUIRED) {
//				err= -1;
//				fprintf(stderr, "%s.%s: missing required parameter %s\n", tid, fid, pid);
//				break;
//			}
//			ofs++;
//			continue;
//		}
//
//		type= RNA_property_type(parm);
//		ftype= format[ofs++];
//		len= RNA_property_array_length(parm);
//		alen= rna_function_format_array_length(format, ofs, flen);
//
//		if (len!=alen) {
//			err= -1;
//			fprintf(stderr, "%s.%s: for parameter %s, was expecting an array of %i elements, passed %i elements instead\n", tid, fid, pid, len, alen);
//			break;
//		}
//
//		switch (type) {
//		case PROP_BOOLEAN:
//		case PROP_INT:
//		case PROP_ENUM:
//			{
//				int arg= va_arg(args, int);
//				err= rna_function_parameter_parse(&funcptr, parm, type, ftype, len, iter.data, &arg, NULL, tid, fid, pid);
//				break;
//			}
//		case PROP_FLOAT:
//			{
//				double arg= va_arg(args, double);
//				err= rna_function_parameter_parse(&funcptr, parm, type, ftype, len, iter.data, &arg, NULL, tid, fid, pid);
//				break;
//			}
//		case PROP_STRING:
//			{
//				char *arg= va_arg(args, char*);
//				err= rna_function_parameter_parse(&funcptr, parm, type, ftype, len, iter.data, &arg, NULL, tid, fid, pid);
//				break;
//			}
//		case PROP_POINTER:
//			{
//				StructRNA *srna= va_arg(args, StructRNA*);
//				void *arg= va_arg(args, void*);
//				err= rna_function_parameter_parse(&funcptr, parm, type, ftype, len, iter.data, &arg, srna, tid, fid, pid);
//				break;
//			}
//		case PROP_COLLECTION:
//			{
//				StructRNA *srna= va_arg(args, StructRNA*);
//				ListBase *arg= va_arg(args, ListBase*);
//				err= rna_function_parameter_parse(&funcptr, parm, type, ftype, len, iter.data, &arg, srna, tid, fid, pid);
//				break;
//			}
//		default:
//			{
//				/* handle errors */
//				err= rna_function_parameter_parse(&funcptr, parm, type, ftype, len, iter.data, NULL, NULL, tid, fid, pid);
//				break;
//			}
//		}
//
//		if (err!=0)
//			break;
//	}
//
//	if (err==0)
//		err= RNA_function_call(C, reports, ptr, func, &parms);
//
//	/* XXX throw error when more parameters than those needed are passed or leave silent? */
//	if (err==0 && pret && ofs<flen && format[ofs++]=='R') {
//		parm= pret;
//
//		type= RNA_property_type(parm);
//		ftype= format[ofs++];
//		len= RNA_property_array_length(parm);
//		alen= rna_function_format_array_length(format, ofs, flen);
//
//		if (len!=alen) {
//			err= -1;
//			fprintf(stderr, "%s.%s: for return parameter %s, was expecting an array of %i elements, passed %i elements instead\n", tid, fid, pid, len, alen);
//		}
//		else {
//			switch (type) {
//			case PROP_BOOLEAN:
//			case PROP_INT:
//			case PROP_ENUM:
//				{
//					int *arg= va_arg(args, int*);
//					err= rna_function_parameter_parse(&funcptr, parm, type, ftype, len, arg, retdata, NULL, tid, fid, pid);
//					break;
//				}
//			case PROP_FLOAT:
//				{
//					float *arg= va_arg(args, float*);
//					err= rna_function_parameter_parse(&funcptr, parm, type, ftype, len, arg, retdata, NULL, tid, fid, pid);
//					break;
//				}
//			case PROP_STRING:
//				{
//					char **arg= va_arg(args, char**);
//					err= rna_function_parameter_parse(&funcptr, parm, type, ftype, len, arg, retdata, NULL, tid, fid, pid);
//					break;
//				}
//			case PROP_POINTER:
//				{
//					StructRNA *srna= va_arg(args, StructRNA*);
//					void **arg= va_arg(args, void**);
//					err= rna_function_parameter_parse(&funcptr, parm, type, ftype, len, arg, retdata, srna, tid, fid, pid);
//					break;
//				}
//			case PROP_COLLECTION:
//				{
//					StructRNA *srna= va_arg(args, StructRNA*);
//					ListBase **arg= va_arg(args, ListBase**);
//					err= rna_function_parameter_parse(&funcptr, parm, type, ftype, len, arg, retdata, srna, tid, fid, pid);
//					break;
//				}
//			default:
//				{
//					/* handle errors */
//					err= rna_function_parameter_parse(&funcptr, parm, type, ftype, len, NULL, NULL, NULL, tid, fid, pid);
//					break;
//				}
//			}
//		}
//	}
//
//	RNA_parameter_list_end(&iter);
//	RNA_parameter_list_free(&parms);
//
//	return err;
//}
//
//int RNA_function_call_direct_va_lookup(bContext *C, ReportList *reports, PointerRNA *ptr, const char *identifier, const char *format, va_list args)
//{
//	FunctionRNA *func;
//
//	func= RNA_struct_find_function(ptr, identifier);
//
//	if(func)
//		return RNA_function_call_direct_va(C, reports, ptr, func, format, args);
//
//	return 0;
//}
}

